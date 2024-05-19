package com.vanky.chat.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author vanky
 * @create 2024/4/13 21:36
 */
public class StringRedisUtil {

    private static StringRedisTemplate stringRedisTemplate;

    static {
        StringRedisUtil.stringRedisTemplate = SpringUtil.getBean("stringRedisTemplate", StringRedisTemplate.class);
    }

    public static void put(String key, String value){
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public static void put(String key, String value, int time, TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public static Boolean ifExisted(String key){
        return stringRedisTemplate.opsForValue().get(key) != null;
    }

    public static void del(Collection<String> key){
        stringRedisTemplate.delete(key);
    }

    public static void del(String key){
        stringRedisTemplate.delete(key);
    }

    public static String get(String shareKeyRedisKey) {
        return stringRedisTemplate.opsForValue().get(shareKeyRedisKey);
    }
}
