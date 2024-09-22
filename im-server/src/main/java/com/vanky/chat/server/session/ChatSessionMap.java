package com.vanky.chat.server.session;


import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vanky
 * @create 2024/3/29 10:18
 * 存连接到当前机器的session,
 * map中有session的说明用户的连接在当前服务端，而且在线
 */
@Component
@Slf4j
public class ChatSessionMap {

    /**
     * 保存用户的id与channel的映射
     */
    public static ConcurrentHashMap<Long, NioSocketChannel> chatSessionMap = new ConcurrentHashMap<>();

    public static void sendMessage(Long userId, BaseMsgProto.BaseMsg msg){
        NioSocketChannel channel = chatSessionMap.get(userId);

        if (channel != null){
            channel.writeAndFlush(msg);
        }else{
            //todo 如果用户不在该服务器或不在线
            log.error("用户已断开连接~~~");
        }
    }
}
