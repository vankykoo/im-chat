package com.vanky.chat.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vanky
 * @create 2024/1/29 15:29
 */
@Configuration
public class MyRedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(){
        Config config = new Config();
        //单节点模式
        config.useSingleServer().setAddress("redis://localhost:6379")
                .setPassword("123456")
                .setDatabase(0);

        RedissonClient redissonClient = Redisson.create(config);

        return redissonClient;
    }

}
