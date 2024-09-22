package com.vanky.chat.client.processor;

import com.vanky.chat.client.utils.MsgGenerator;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author vanky
 * @create 2024/9/22 11:19
 */
@Component
@Slf4j
public class LoginMsgProcessor {

    @Resource
    private MsgGenerator msgGenerator;

    /**
     * 向服务端发送登录消息
     * @param userId
     * @param channel
     */
    public void sendLoginMsg(Long userId, NioSocketChannel channel){
        //这里放一个登录的消息
        BaseMsgProto.BaseMsg msg = msgGenerator.generateLoginMsg(userId);
        channel.writeAndFlush(msg);
    }

    /**
     * 向服务端发送退出登录消息
     * @param userId
     * @param channel
     */
    public void sendLogoutMsg(Long userId, NioSocketChannel channel){
        BaseMsgProto.BaseMsg msg = msgGenerator.generateLogoutMsg(userId);
        channel.writeAndFlush(msg);
    }


}
