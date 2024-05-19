package com.vanky.chat.mq.service;

import com.rabbitmq.client.Channel;
import com.vanky.chat.common.bo.CanalModel;
import com.vanky.chat.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author vanky
 * @create 2024/4/21 20:35
 */
@Component
@Slf4j
public class CanalRedisService {

    /**
     * 插入数据
     */
    @RabbitListener(queues = {"canal.redis.insert.queue"})
    public void CanalRedisInsertListener(Message message, Channel channel, CanalModel canalModel){

        String key = "im:data:" + canalModel.getTableName() + ":" + canalModel.getDataId();

        log.info("redis 【插入】 数据：{}", key);
        RedisUtil.put(key, canalModel.getAfter());

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新数据
     */
    @RabbitListener(queues = {"canal.redis.update.queue"})
    public void CanalRedisUpdateListener(Message message, Channel channel, CanalModel canalModel){

        String key = "im:data:" + canalModel.getTableName() + ":" + canalModel.getDataId();

        log.info("redis 【更新】 数据：{}", key);
        RedisUtil.put(key, canalModel.getAfter());

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除数据
     */
    @RabbitListener(queues = {"canal.redis.delete.queue"})
    public void CanalRedisDeleteListener(Message message, Channel channel, CanalModel canalModel){
        String key = "im:data:" + canalModel.getTableName() + ":" + canalModel.getDataId();

        log.info("redis 【删除】 数据：{}", key);
        RedisUtil.del(key);

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
