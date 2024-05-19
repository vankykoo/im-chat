package com.vanky.chat.auth.service;

import com.vanky.chat.auth.pojo.bo.GiteeUserInfo;
import com.vanky.chat.common.bo.CheckRedisTokenBo;
import com.vanky.chat.common.bo.RefreshTokenInfoBo;
import com.vanky.chat.common.to.UserTo;
import com.vanky.chat.common.vo.LoginSuccessVo;

import java.util.Map;

/**
 * @author vanky
 * @create 2024/5/10 20:40
 */
public interface AuthService {

    /**
     * 用户登录
     * @param userTo
     * @return
     */
    LoginSuccessVo userLogin(UserTo userTo);

    /**
     * 检查并删除过期token
     * @param checkRedisTokenBo
     */
    void checkUserTokens(CheckRedisTokenBo checkRedisTokenBo);

    /**
     * gitee社交登录
     * @param giteeUserInfo
     * @return
     */
    LoginSuccessVo giteeLogin(GiteeUserInfo giteeUserInfo);
}
