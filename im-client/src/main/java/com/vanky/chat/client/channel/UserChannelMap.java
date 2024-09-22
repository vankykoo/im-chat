package com.vanky.chat.client.channel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.vanky.chat.client.handler.ReconnectHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vanky
 * @create 2024/3/30 11:28
 */
public class UserChannelMap {

    public static final ReconnectHandler reconnectHandler;

    static {
        reconnectHandler = SpringUtil.getBean("reconnectHandler", ReconnectHandler.class);
    }

    /**
     * 连接唯一标识id与用户id的映射
     */
    public static ConcurrentHashMap<String, Long> channelUserMap = new ConcurrentHashMap<>();

    /**
     * 用户id 与 连接 的映射
     */
    public static ConcurrentHashMap<Long, NioSocketChannel> userChannel = new ConcurrentHashMap<>();

    //
    public static ConcurrentHashMap<Long, NioSocketChannel> groupChannel = new ConcurrentHashMap<>();

    //根据服务端地址来建立映射关系
    public static ConcurrentHashMap<String, NioSocketChannel> hostChannel = new ConcurrentHashMap<>();

    public static NioSocketChannel getGroupChannel(Long userId){
        NioSocketChannel channel = groupChannel.get(userId);

        return checkChannel(channel, userId, 1);
    }

    public static NioSocketChannel getChannel(Long userId){
        NioSocketChannel channel = userChannel.get(userId);

        return checkChannel(channel, userId, 0);
    }

    public static NioSocketChannel checkChannel(NioSocketChannel channel, Long userId, int type){
        if (channel == null){
            //未连接
            channel = reconnectHandler.reconnect(userId, type);
        }else if (!channel.isActive()){
            //断开连接了
            userChannel.remove(userId, channel);
            channelUserMap.remove(channel.id().asLongText());

            //重连
            channel = reconnectHandler.reconnect(userId, type);
        }

        return channel;
    }
}
