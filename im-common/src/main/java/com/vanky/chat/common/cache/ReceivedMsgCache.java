package com.vanky.chat.common.cache;

/**
 * @author vanky
 * @create 2024/4/13 23:36
 */
public class ReceivedMsgCache {

    //存储时间为10分钟
    public static final int CACHE_TIME = 10;

    public static final String CLIENT_RECEIVED_MSG_CACHE = "client_received_msg_id:";

    public static final String SERVER_RECEIVED_MSG_CACHE = "server_received_msg_id:";

    public static String getClientKey(Long userId, Long uniqueId){
        return CLIENT_RECEIVED_MSG_CACHE + uniqueId + ":" + userId;
    }

    public static String getServerKey(Long uniqueId){
        return SERVER_RECEIVED_MSG_CACHE + uniqueId;
    }

}
