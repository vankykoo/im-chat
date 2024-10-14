package com.vanky.chat.server.utils;

import com.vanky.chat.common.bo.GlobalChatSessionBo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vanky
 * @create 2024/10/14 20:43
 */
@Slf4j
public class LogUtil {

    public static void logging2Login(GlobalChatSessionBo globalChatSessionBo, String channelId){
        log.info("****************用户登录****************");
        log.info("用户 id ：{}", globalChatSessionBo.getUserId());
        log.info("客户端 Host ：{}", globalChatSessionBo.getHost());
        log.info("客户端 port ：{}", globalChatSessionBo.getPort());
        log.info("channel id ：{}", channelId);
        log.info("session id ：{}", globalChatSessionBo.getSessionUid());
        log.info("***************************************");
    }
}
