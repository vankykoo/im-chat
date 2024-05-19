package com.vanky.chat.server.listener;

import com.vanky.chat.common.cache.WaitingAckCache;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.common.utils.StringRedisUtil;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.session.ChatSessionMap;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author vanky
 * @create 2024/4/2 22:46
 */
@Component
@Slf4j
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
        String body = stringSerializer.deserialize(message.getBody());//waiting_ack:a1d51aeb-89d5-4a88-bd53-0073cd159a99
        String channel = stringSerializer.deserialize(message.getChannel());//__keyevent@0__:expired

        String[] split = body.split(":");

        if ("__keyevent@0__:expired".equals(channel) && "waiting_ack".equals(split[0])
                && !"detail".equals(split[1])){

            log.info(body + "  过期了 ");
            //已经重试的次数
            int retryTimes = Integer.parseInt(split[2]);

            String key = "";

            int waitTime = WaitingAckCache.WAITING_TIME_FIRST_RETRY;

            if (retryTimes == 2){
                String msgCache = redisTemplate.opsForValue()
                        .get(WaitingAckCache.WAITING_ACK_DETAIL_MSG + split[1]).toString();

                System.out.println(msgCache);

                throw new MyException.MessageSendException();
            } else{
                if (retryTimes == 1){
                    waitTime = WaitingAckCache.WAITING_TIME_SECOND_RETRY;
                }

                key = WaitingAckCache.WAITING_ACK_CACHE_NAME + split[1] + ":" + (retryTimes + 1);
            }

            //从数据库获取消息
            BaseMsg baseMsg = null;

            String cacheKey = WaitingAckCache.WAITING_ACK_DETAIL_MSG + split[1];
            baseMsg = RedisUtil.get(cacheKey, BaseMsg.class);

            if(baseMsg == null){
                log.warn("查询内容已被清空，可能是已经发送成功！消息cacheKey为：{}",cacheKey);
                return;
            }

            //重发消息
            ChatSessionMap.sendMessage(baseMsg.getToUserId(), BaseMsg.BaseMsg2Proto(baseMsg));
            StringRedisUtil.put(key, "", waitTime, TimeUnit.SECONDS);
        }

    }
}
