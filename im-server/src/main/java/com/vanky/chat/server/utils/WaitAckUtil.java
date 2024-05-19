package com.vanky.chat.server.utils;

import com.vanky.chat.common.bo.BaseMsgBo;
import com.vanky.chat.common.cache.WaitingAckCache;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.common.utils.StringRedisUtil;
import com.vanky.chat.server.pojo.BaseMsg;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 缓存等待ack消息
 * @author vanky
 * @create 2024/4/12 11:06
 */
@Component
@Slf4j
public class WaitAckUtil {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 保存用于等待ack的key，并保存发送的消息体（用于重发）
     * @param baseMsg
     */
    public void saveWaitingAckMsgDetail(BaseMsgProto.BaseMsg baseMsg, Long uniqueId){
        String uniqueIdAndRetryTime = uniqueId + ":";

        //2.保存发送的消息体（用于重发）
        String cacheKey = WaitingAckCache.WAITING_ACK_DETAIL_MSG + uniqueId;
        if (RedisUtil.hasExisted(cacheKey)){
            //消息体已经存在了，说明这次是重发
            uniqueIdAndRetryTime += "1";
        }else{
            //消息体不存在，说明是第一次发送
            uniqueIdAndRetryTime += "0";

            BaseMsg msg = BaseMsg.Proto2BaseMsg(baseMsg);
            BaseMsgBo baseMsgBo = new BaseMsgBo();
            BeanUtils.copyProperties(msg, baseMsgBo);

            RedisUtil.put(cacheKey, baseMsgBo, WaitingAckCache.WAITING_ACK_DETAIL_TIME, TimeUnit.MINUTES);
        }

        //保存用于等待ack的key 到 rabbitmq的延迟队列中
        rabbitTemplate.convertAndSend("wait-msg-ack-exchange",
                "wait.msg.ack.private",
                uniqueIdAndRetryTime);
        log.info("保存等待ack消息到延迟队列：{}", uniqueIdAndRetryTime);
    }

    /**
     * 删除等待ack缓存的消息体
     * @param uniqueId
     */
    public void deleteWaitingAckMsgDetail(String uniqueId){
        String cacheKey = WaitingAckCache.WAITING_ACK_DETAIL_MSG + uniqueId;
        StringRedisUtil.del(cacheKey);

        log.info("删除了消息的缓存：{}", cacheKey);
    }

    /**
     * 获取等待ack的消息体
     * @param uniqueId
     * @return
     */
    public BaseMsg getWaitingAckMsgDetail(String uniqueId){
        String cacheKey = WaitingAckCache.WAITING_ACK_DETAIL_MSG + uniqueId;
        return RedisUtil.get(cacheKey, BaseMsg.class);
    }
}
