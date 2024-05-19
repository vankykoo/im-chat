package com.vanky.chat.auth.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.vanky.chat.auth.config.SecurityOauthGiteeProperties;
import com.vanky.chat.auth.manager.TokenManager;
import com.vanky.chat.auth.pojo.bo.AccessTokenInfo;
import com.vanky.chat.auth.pojo.bo.GiteeUserInfo;
import com.vanky.chat.auth.pojo.bo.OAuthLoginSuccessBo;
import com.vanky.chat.auth.service.AuthService;
import com.vanky.chat.common.bo.AccessTokenInfoBO;
import com.vanky.chat.common.bo.CheckRedisTokenBo;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.to.UserTo;
import com.vanky.chat.common.vo.LoginSuccessVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vanky
 * @create 2024/5/16 16:55
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证相关接口")
@Slf4j
public class AuthController {

    @Resource
    private TokenManager tokenManager;

    @Resource
    private AuthService authService;

    @Resource
    private SecurityOauthGiteeProperties properties;

    @PostMapping("/login")
    @Operation(summary = "登录接口")
    public Result<LoginSuccessVo> login(@RequestBody UserTo userTo){
        return Result.success(authService.userLogin(userTo));
    }

    @GetMapping("/oauth/gitee/login")
    @Operation(summary = "gitee社交登录")
    public Result<LoginSuccessVo> giteeLogin(@RequestParam("code") String code){
        Map<String, Object> map = new HashMap<>();
        map.put("grant_type", properties.getGrantType());
        map.put("code", code);
        map.put("client_id", properties.getClientId());
        map.put("client_secret", properties.getClientSecret());
        map.put("redirect_uri", properties.getRedirectUri());

        String getTokenVo = HttpUtil.post(properties.getTokenUri(), map);

        OAuthLoginSuccessBo oAuthLoginSuccessBo = JSONUtil.toBean(getTokenVo, OAuthLoginSuccessBo.class);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("access_token", oAuthLoginSuccessBo.getAccessToken());

        String userInfoBo = HttpUtil.get(properties.getUserInfoUri(), map2);
        GiteeUserInfo giteeUserInfo = JSONUtil.toBean(userInfoBo, GiteeUserInfo.class);

        return Result.success(authService.giteeLogin(giteeUserInfo));
    }

    /**
     * 生成accessToken
     * @param userId
     * @return
     */
    @GetMapping("/generateAccessToken")
    @Operation(summary = "生成新的accessToken")
    public Result<AccessTokenInfoBO> generateAccessToken(@RequestParam("userId") Long userId){
        //生成 accessToken
        String accessToken = tokenManager.createAccessToken(userId);
        //获取token中的用户相关信息
        AccessTokenInfo infoFromAccessToken = tokenManager.getInfoFromAccessToken(accessToken);
        AccessTokenInfoBO accessTokenInfoBO = new AccessTokenInfoBO();
        BeanUtils.copyProperties(infoFromAccessToken, accessTokenInfoBO);
        accessTokenInfoBO.setToken(accessToken);

        return Result.success(accessTokenInfoBO);
    }

    /**
     * 获取 token 中用户的信息
     * 在这个接口中判断token是否过期，无感更新，所以返回的东西需要带上token
     * @param accessToken
     * @return
     */
    @GetMapping("/getTokenInfo")
    @Operation(summary = "根据token获取用户信息")
    public Result<AccessTokenInfoBO> getTokenInfo(@RequestParam("accessToken") String accessToken){
        AccessTokenInfo infoFromAccessToken = tokenManager.getInfoFromAccessToken(accessToken);
        AccessTokenInfoBO accessTokenInfoBO = new AccessTokenInfoBO();
        BeanUtils.copyProperties(infoFromAccessToken, accessTokenInfoBO);

        return Result.success(accessTokenInfoBO);
    }

    /**
     * 删除用户所有的 token
     * @param userId
     * @return
     */
    @DeleteMapping("/deleteAllTokens")
    @Operation(summary = "删除用户所有的token")
    public Result deleteAllTokens(@RequestParam("userId") Long userId){
        tokenManager.deleteUserAllTokens(userId);

        return Result.success();
    }

    @PostMapping("/checkAllTokens")
    @Operation(summary = "检查并删除过期token")
    public Result checkAllTokens(@RequestBody CheckRedisTokenBo checkRedisTokenBo){
        authService.checkUserTokens(checkRedisTokenBo);

        return Result.success();
    }
}
