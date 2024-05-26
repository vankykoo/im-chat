package com.vanky.chat.server.handler;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author vanky
 * @create 2024/5/25 11:25
 */
@Component
@Slf4j
public class HeatBeatServerHandler{
    protected void handle(ChannelHandlerContext ctx, String msg){
        if ("ping".equals(msg)) {
            log.info("收到客户端的心跳包 ping");
            ctx.channel().writeAndFlush("pong");
        }
    }
}
