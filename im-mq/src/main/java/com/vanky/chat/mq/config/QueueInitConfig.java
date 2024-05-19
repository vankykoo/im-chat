package com.vanky.chat.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vanky
 * @create 2024/4/2 16:22
 */
@Configuration
public class QueueInitConfig {

    /**
     * 等待私信ack消息的延迟队列
     * @return
     */
    @Bean
    public Queue WaitMsgAckPrivateQueue(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","dead-letter-exchange");
        arguments.put("x-dead-letter-routing-key","msg.delay");
        arguments.put("x-message-ttl",3000);//3s

        return new Queue("wait.msg.ack.private.queue", true, false, false, arguments);
    }

    /**
     * 等待私信ack消息的绑定
     * @return
     */
    @Bean
    public Binding WaitMsgAckPrivateBinding(){
        return new Binding("wait.msg.ack.private.queue",
                Binding.DestinationType.QUEUE,
                "wait-msg-ack-exchange",
                "wait.msg.ack.private",
                null);
    }

    /**
     * 等待群聊消息ack消息的延迟队列
     * @return
     */
    @Bean
    public Queue WaitMsgAckGroupQueue(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","dead-letter-exchange");
        arguments.put("x-dead-letter-routing-key","msg.delay");
        arguments.put("x-message-ttl",3000);

        return new Queue("wait.msg.ack.group.queue", true, false, false, arguments);
    }

    /**
     * 等待群聊ack消息的绑定
     * @return
     */
    @Bean
    public Binding WaitMsgAckGroupBinding(){
        return new Binding("wait.msg.ack.group.queue",
                Binding.DestinationType.QUEUE,
                "wait-msg-ack-exchange",
                "wait.msg.ack.group",
                null);
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue MsgDelayQueue(){
        return new Queue("msg.delay.queue", true, false, false);
    }

    /**
     * 死信队列和死信交换机绑定
     * @return
     */
    @Bean
    public Binding MsgDelayBinding(){
        return new Binding("msg.delay.queue",
                Binding.DestinationType.QUEUE,
                "dead-letter-exchange",
                "msg.delay",
                null);
    }

    /**
     * canal数据同步队列 ===》 insert
     * @return
     */
    @Bean
    public Queue CanalInsertQueue(){
        return new Queue("canal.redis.insert.queue", true, false, false);
    }

    /**
     * mysql 插入数据时更新redis
     * @return
     */
    @Bean
    public Binding CanalInsertBinding(){
        return new Binding("canal.redis.insert.queue",
                Binding.DestinationType.QUEUE,
                "canal-redis-exchange",
                "canal.redis.insert",
                null);
    }

    /**
     * canal数据同步队列 ===》 update
     * @return
     */
    @Bean
    public Queue CanalUpdateQueue(){
        return new Queue("canal.redis.update.queue", true, false, false);
    }

    /**
     * mysql 修改数据时更新redis
     * @return
     */
    @Bean
    public Binding CanalUpdateBinding(){
        return new Binding("canal.redis.update.queue",
                Binding.DestinationType.QUEUE,
                "canal-redis-exchange",
                "canal.redis.update",
                null);
    }

    /**
     * canal数据同步队列 ===》 delete
     * @return
     */
    @Bean
    public Queue CanalDeleteQueue(){
        return new Queue("canal.redis.delete.queue", true, false, false);
    }

    /**
     * mysql 删除数据时更新redis
     * @return
     */
    @Bean
    public Binding CanalDeleteBinding(){
        return new Binding("canal.redis.delete.queue",
                Binding.DestinationType.QUEUE,
                "canal-redis-exchange",
                "canal.redis.delete",
                null);
    }

    /**
     * 检查用户token是否过期，并删除过期的token
     * @return
     */
    @Bean
    public Queue AuthTokenCheckQueue(){
        return new Queue("auth.token.check.queue", true, false, false);
    }

    /**
     * AuthTokenCheckQueue 与 AuthTokenExchange绑定
     * @return
     */
    @Bean
    public Binding AuthTokenCheckBinding(){
        return new Binding("auth.token.check.queue",
                Binding.DestinationType.QUEUE,
                "auth-token-exchange",
                "auth.token.check",
                null);
    }

}
