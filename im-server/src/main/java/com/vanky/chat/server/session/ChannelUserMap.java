package com.vanky.chat.server.session;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vanky
 * @create 2024/5/26 21:41
 */
@Component
public class ChannelUserMap {
    public static ConcurrentHashMap<String, Long> channelUserMap = new ConcurrentHashMap<>();
}
