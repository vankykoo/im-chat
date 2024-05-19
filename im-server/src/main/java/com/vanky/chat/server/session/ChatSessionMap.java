package com.vanky.chat.server.session;


import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vanky
 * @create 2024/3/29 10:18
 * 存连接到当前机器的session,
 * map中有session的说明用户的连接在当前服务端，而且在线
 */
@Component
public class ChatSessionMap {

    public static ConcurrentHashMap<Long, ChatSession> chatSessionMap = new ConcurrentHashMap<>();

    public static void sendMessage(Long userId, BaseMsgProto.BaseMsg msg){
        ChatSession chatSession = chatSessionMap.get(userId);

        if (chatSession != null && chatSession.getChannel() != null){
            Channel nioSocketChannel = chatSession.getChannel();
            nioSocketChannel.writeAndFlush(msg);
        }else{
            //todo 如果用户不在该服务器或不在线
        }
    }
}
