package com.vanky.chat.server.utils;

import com.vanky.chat.common.cache.ChannelCache;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.session.ChatSession;
import com.vanky.chat.server.session.ChatSessionMap;
import com.vanky.chat.server.session.GlobalChatSession;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author vanky
 * @create 2024/3/29 10:20
 */
public class SessionUtil {

    public void put(Long userId, ChatSession chatSession){
        ChatSessionMap.chatSessionMap.put(userId, chatSession);

        //存到redis中
        String key = ChannelCache.GLOBAL_CHAT_SESSION_PREFIX + userId;
        GlobalChatSession globalChatSession = chatSession2GlobalChatSession(chatSession);

        RedisUtil.sput(key, globalChatSession);
    }


    /**
     * chatSession转换为可以存到redis中的全局chatSession格式
     * @param chatSession
     * @return
     */
    private GlobalChatSession chatSession2GlobalChatSession(ChatSession chatSession){
        NioSocketChannel channel = (NioSocketChannel) chatSession.getChannel();

        GlobalChatSession globalChatSession = new GlobalChatSession(channel.remoteAddress().getHostString(),
                channel.remoteAddress().getPort(),
                chatSession.getSessionUid(),
                chatSession.getUserBo().getId());

        return globalChatSession;
    }

}
