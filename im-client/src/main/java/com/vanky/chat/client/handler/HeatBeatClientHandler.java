package com.vanky.chat.client.handler;

import com.vanky.chat.client.channel.UserChannelMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author vanky
 * @create 2024/5/25 11:33
 */
@Component
@Slf4j
public class HeatBeatClientHandler{
    protected void handle(ChannelHandlerContext ctx, String msg){
        if ("pong".equals(msg)) {
            log.info("接收到服务端的心跳包 pong");
        }
    }
}
