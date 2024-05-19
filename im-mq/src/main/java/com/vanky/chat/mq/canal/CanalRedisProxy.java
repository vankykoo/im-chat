package com.vanky.chat.mq.canal;

import com.vanky.chat.common.bo.CanalModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author vanky
 * @create 2024/4/21 20:39
 */
@Component
@Slf4j
public class CanalRedisProxy {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void putMQ(CanalModel canalModel) {
        String eventType = canalModel.getEventType();

        if (!StringUtils.hasText(eventType)){
            throw new NullPointerException("内容为空！");
        }

        String routingKey = "";
        if ("DELETE".equals(eventType)){
            routingKey = "canal.redis.delete";
        }else if ("UPDATE".equals(eventType)){
            routingKey = "canal.redis.update";
        } else if ("INSERT".equals(eventType)) {
            routingKey = "canal.redis.insert";
        }

        rabbitTemplate.convertAndSend("canal-redis-exchange", routingKey, canalModel);
    }
}
