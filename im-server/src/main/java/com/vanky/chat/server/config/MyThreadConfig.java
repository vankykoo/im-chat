package com.vanky.chat.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author vanky
 * @create 2024/2/19 14:08
 * 线程池配置
 */
@Configuration
public class MyThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool){
        return new ThreadPoolExecutor(pool.getCoreSize(), //线程池核心线程大小
                pool.getMaxSize(), //最大线程数量
                pool.getKeepAliveTime(), //线程最大存活时间
                TimeUnit.SECONDS,   //存活时间单位
                new LinkedBlockingDeque<>(100000), //阻塞队列
                Executors.defaultThreadFactory(), //使用默认线程创建工厂
                new ThreadPoolExecutor.AbortPolicy());
    }

}
