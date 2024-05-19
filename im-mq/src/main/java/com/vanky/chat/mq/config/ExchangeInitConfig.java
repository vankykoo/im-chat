package com.vanky.chat.mq.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vanky
 * @create 2024/4/2 16:22
 */
@Configuration
public class ExchangeInitConfig {

    /**
     * 死信交换机
     * @return
     */
    @Bean
    public Exchange DeadLetterExchange(){
        return new TopicExchange("dead-letter-exchange", true, false);
    }

    /**
     * 等待消息ack交换机
     * @return
     */
    @Bean
    public Exchange WaitMsgAckExchange(){
        return new TopicExchange("wait-msg-ack-exchange", true, false);
    }


    /**
     * canal mysql数据同步redis交换机
     * @return
     */
    @Bean
    public Exchange CanalRedisExchange(){
        return new TopicExchange("canal-redis-exchange", true, false);
    }

    /**
     * 处理redis中过期token的交换机
     * @return
     */
    @Bean
    public Exchange AuthTokenExchange(){
        return new TopicExchange("auth-token-exchange", true, false);
    }

}
