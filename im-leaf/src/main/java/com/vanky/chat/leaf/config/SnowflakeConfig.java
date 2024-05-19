package com.vanky.chat.leaf.config;

import com.vanky.chat.leaf.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vanky
 * @create 2024/4/13 20:55
 */
@Configuration
public class SnowflakeConfig {

    /**
     * 工作机器ID(0~31)
     */
    @Value("${worker-id}")
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    @Value("${datacenter-id}")
    private long datacenterId;

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker(){
        return new SnowflakeIdWorker(workerId, datacenterId);
    }

}
