package com.vanky.chat.auth.service.impl;

import com.vanky.chat.auth.manager.TokenManager;
import com.vanky.chat.auth.pojo.bo.GiteeUserInfo;
import com.vanky.chat.auth.service.AuthService;
import com.vanky.chat.common.bo.CheckRedisTokenBo;
import com.vanky.chat.common.bo.ImUserBo;
import com.vanky.chat.common.bo.RefreshTokenInfoBo;
import com.vanky.chat.common.cache.RedisSimpleCacheName;
import com.vanky.chat.common.exception.MyException.*;
import com.vanky.chat.common.feign.nettyclientfeign.NettyClientFeignClient;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.to.UserTo;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.common.vo.LoginSuccessVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vanky
 * @create 2024/5/10 20:41
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private NettyClientFeignClient nettyClientFeignClient;

    @Resource
    private TokenManager tokenManager;

    @Resource
    private ImUserFeignClient imUserFeignClient;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginSuccessVo userLogin(UserTo userTo) {
        Result<ImUserBo> result = imUserFeignClient.getUserByUserName(userTo.getUsername());
        if (!result.isSuccess()){
            throw new FeignProcessException();
        }

        ImUserBo imUser = result.getData();
        //判断密码是否正确
        boolean matches = passwordEncoder.matches(userTo.getPassword(), imUser.getPassword());
        if (!matches){
            //密码错误
            throw new PasswordErrorException();
        }

        log.info("【{}】 用户登录成功！", imUser.getUsername());

        //登录成功后，连接到客户端
        nettyClientFeignClient.connect(imUser.getUserId());

        //根据用户信息生成token
        String accessToken = tokenManager.createAccessToken(imUser.getUserId());

        return new LoginSuccessVo(imUser.getUserId(), imUser.getUsername(), accessToken);
    }

    @Override
    public void checkUserTokens(CheckRedisTokenBo checkRedisTokenBo) {
        Map<String, RefreshTokenInfoBo> tokens = checkRedisTokenBo.getTokens();

        List<String> expiredTokenKeys = new ArrayList<>();
        List<String> expiredTokenMappingKeys = new ArrayList<>();

        tokens.forEach((k, v) -> {
            Long expireIn = v.getExpireIn();
            long currentTime = System.currentTimeMillis();

            if (expireIn < currentTime){
                //已经过期了，删除该token和与refreshToken的映射
                expiredTokenKeys.add(k);
                expiredTokenMappingKeys.add(RedisSimpleCacheName.REFRESH_TOKEN + k);
            }
        });

        if (expiredTokenKeys.size() > 0){
            RedisUtil.hdel(checkRedisTokenBo.getAccessTokenKey(), expiredTokenKeys);
            RedisUtil.del(expiredTokenMappingKeys);
        }
    }

    @Override
    public LoginSuccessVo giteeLogin(GiteeUserInfo giteeUserInfo) {
        String username = "gitee_" + giteeUserInfo.getId();

        Result<ImUserBo> result = imUserFeignClient.getUserByUserName(username);
        if (!result.isSuccess()){
            throw new FeignProcessException();
        }

        if (result.getData() == null){
            //用户为空，系统自动注册
            //随机生成密码
            String randomSecret = UUID.randomUUID().toString().replaceAll("-", "");
            UserTo userTo = new UserTo(username, randomSecret);
            imUserFeignClient.register(userTo);
            log.info("gitee 用户 {} 第一次登录，系统自动注册成功！", userTo.getUsername());

            result = imUserFeignClient.getUserByUserName(username);
            if (!result.isSuccess()){
                throw new FeignProcessException();
            }
        }

        log.info("【{}】 用户登录成功！", username);

        ImUserBo imUser = result.getData();

        //登录成功后，连接到客户端
        nettyClientFeignClient.connect(imUser.getUserId());

        //根据用户信息生成token
        String accessToken = tokenManager.createAccessToken(imUser.getUserId());

        return new LoginSuccessVo(imUser.getUserId(), imUser.getUsername(), accessToken);
    }
}
