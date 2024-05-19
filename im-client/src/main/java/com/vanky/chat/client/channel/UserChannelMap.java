package com.vanky.chat.client.channel;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vanky
 * @create 2024/3/30 11:28
 */
public class UserChannelMap {

    public static ConcurrentHashMap<Integer, Long> context = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Long, NioSocketChannel> userChannel = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Long, NioSocketChannel> groupChannel = new ConcurrentHashMap<>();

    //根据服务端地址来建立映射关系
    public static ConcurrentHashMap<String, NioSocketChannel> hostChannel = new ConcurrentHashMap<>();
}
