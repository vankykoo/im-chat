package com.vanky.chat.common.cache;

/**
 * @author vanky
 * @create 2024/4/8 20:14
 */
public class WaitingAckCache {

    /**
     * 1、等待ack的key的格式：【waiting_ack:(uniqueId):(重试次数)】
     * 2、存储重发消息的key的格式：【waiting_ack:detail:(uniqueId)】
     */

    public static final String WAITING_ACK_CACHE_NAME = "waiting_ack:";

    public static final String WAITING_ACK_DETAIL_MSG = WAITING_ACK_CACHE_NAME + "detail:";

    public static final Integer WAITING_TIME = 3;//单位：秒

    public static final Integer WAITING_TIME_FIRST_RETRY = 10;//单位：秒

    public static final Integer WAITING_TIME_SECOND_RETRY = 30;//单位：秒

    public static final Integer WAITING_ACK_DETAIL_TIME = 5;//单位：分钟

}
