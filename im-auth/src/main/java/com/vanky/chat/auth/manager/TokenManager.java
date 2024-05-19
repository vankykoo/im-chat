package com.vanky.chat.auth.manager;

import com.vanky.chat.auth.pojo.bo.AccessTokenInfo;
import com.vanky.chat.auth.pojo.bo.RefreshTokenInfo;
import com.vanky.chat.auth.pojo.bo.TokenGenerateBo;
import com.vanky.chat.auth.utils.ImJwtUtil;
import com.vanky.chat.common.bo.ImUserBo;
import com.vanky.chat.common.exception.MyException.*;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.vanky.chat.common.constant.NumberConstant.ACCESS_TOKEN_EXPIRE_TIME;
import static com.vanky.chat.common.constant.NumberConstant.REFRESH_TOKEN_EXPIRE_TIME;

/**
 * 管理 token 信息
 *
 * @author vanky
 * @create 2024/5/15 20:45
 */
@Component
public class TokenManager {

    @Resource
    private ImUserFeignClient imUserFeignClient;

    @Resource
    private ImJwtUtil imJwtUtil;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 生成并保存token
     * 返回 accessToken
     * refreshToken 保存到客户端
     */
    public String createAccessToken(Long userId) {
        //1.根据用户 id 生成 accessToken 和 refreshToken
        Result<ImUserBo> result = imUserFeignClient.getUserById(userId);
        if (!result.isSuccess() || result.getData() == null) {
            throw new FeignProcessException();
        }

        ImUserBo user = result.getData();

        TokenGenerateBo accessToken = generateAccessToken(user.getUserId(), user.getPermissions());
        TokenGenerateBo refreshToken = generateRefreshToken(user.getUserId());

        //2.保存到 redis 中
        saveTokens2Redis(userId, accessToken, refreshToken);

        //3.异步检查用户refreshToken，并删除过期的refreshToken
        rabbitTemplate.convertAndSend("auth-token-exchange", "auth.token.check", userId);

        return accessToken.getToken();
    }

    private void saveTokens2Redis(Long userId, TokenGenerateBo accessToken, TokenGenerateBo refreshToken) {
        //2.1 保存accessToken 到用户map中
        String accessTokenCacheKey = "user_token:" + userId;
        RefreshTokenInfo refreshTokenInfo =
                new RefreshTokenInfo(refreshToken.getToken(), refreshToken.getExpireIn());

        RedisUtil.hput(accessTokenCacheKey, accessToken.getToken(), refreshTokenInfo);

        //2.2 保存 accessToken 和 refreshToken 映射信息
        String mappingKey = "access_refresh_mapping:" + accessToken.getToken();
        long expireIn = refreshTokenInfo.getExpireIn() - System.currentTimeMillis();
        RedisUtil.put(mappingKey, refreshTokenInfo.getRefreshToken(),
                Integer.parseInt(Long.toString(expireIn)), TimeUnit.MILLISECONDS);
    }

    /**
     * 生成 accessToken
     *
     * @param userId
     * @param permissions
     * @return
     */
    private TokenGenerateBo generateAccessToken(Long userId, List<String> permissions) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("permissions", permissions);

        return imJwtUtil.createToken(map, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成 refreshToken
     *
     * @param userId
     * @return
     */
    private TokenGenerateBo generateRefreshToken(Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        return imJwtUtil.createToken(map, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     * 处理过期 accessToken
     *
     * @param accessToken
     * @return
     */
    public String processExpireAccessToken(String accessToken) {
        //1.到redis 映射信息中获取对应的 refreshToken
        String mappingKey = "access_refresh_mapping:" + accessToken;
        String refreshToken = RedisUtil.get(mappingKey, String.class);
        if (refreshToken == null) {
            //refreshToken已过期，报异常，提醒重新登录
            throw new TokenExpiredException();
        }

        //2.解析 refreshToken
        Claims claims = imJwtUtil.parseToken(refreshToken);
        Long userId = (Long) claims.get("userId");

        //3.refreshToken 未过期时，生成新的accessToken；
        String newAccessToken = createAccessToken(userId);

        //4.删除过期的 accessToken 的相关缓存
        RedisUtil.del(mappingKey);

        String userAccessTokenMapKey = "user_token:" + userId;
        RedisUtil.hdel(userAccessTokenMapKey, accessToken);

        return newAccessToken;
    }

    /**
     * 删除用户的所有token
     */
    public void deleteUserAllTokens(Long userId) {
        //获取用户拥有的accessToken
        String userTokenMapKey = "user_token:" + userId;
        Map<String, RefreshTokenInfo> map = RedisUtil.hgetAll(userTokenMapKey, RefreshTokenInfo.class);
        //删除映射关系
        List<String> accessTokens = new ArrayList<>();
        map.forEach((k, v) -> {
            String tokenMappingKey = "access_refresh_mapping:" + k;
            accessTokens.add(tokenMappingKey);
        });

        RedisUtil.del(accessTokens);
        //删除所有accessToken
        RedisUtil.del(userTokenMapKey);
    }

    /**
     * 获取 accessToken 中的用户信息
     */
    public AccessTokenInfo getInfoFromAccessToken(String accessToken) {
        Claims claims = null;
        boolean updateTag = false;
        try{
            claims = imJwtUtil.parseToken(accessToken);
        }catch (ExpiredJwtException e){
            //accessToken过期
            try{
                accessToken = processExpireAccessToken(accessToken);
                claims = imJwtUtil.parseToken(accessToken);
                updateTag = true;
            }catch (TokenExpiredException expiredException){
                //refreshToken 已经过期，需要重新登录
                throw expiredException;
            }
        }

        Long userId = (Long) claims.get("userId");
        List<String> permissions = (List<String>) claims.get("permissions");
        long expireTime = claims.getExpiration().getTime();

        return new AccessTokenInfo(userId, permissions, accessToken, expireTime, updateTag);
    }

}
