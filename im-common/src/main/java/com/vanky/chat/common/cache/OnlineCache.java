package com.vanky.chat.common.cache;

/**
 * @author vanky
 * @create 2024/3/31 22:43
 */
public class OnlineCache {

    public static final String ONLINE_PREFIX = "online:";

    public static final String GROUP_ONLINE_USER = ONLINE_PREFIX + "group:";

    // 客户端的用户在线状态列表
    public static final String FRIEND_ID_LIST = ONLINE_PREFIX + "friends" + RedisSimpleCacheName.UNION_KEY;

}
