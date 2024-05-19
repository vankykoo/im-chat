package com.vanky.chat.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author vanky
 * @create 2024/4/13 21:36
 */
public class RedisUtil {

    private static RedisTemplate<Object, Object> redisTemplate;

    static {
        RedisUtil.redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
    }

    public static void put(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    public static void put(String key, Object value, int time, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public static <T> T get(String key, Class<T> tClass){
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null){
            return null;
        }

        return toOneBean(obj, tClass);
    }

    public static <T> List<T> multiGet(Collection<String> keys, Class<T> tClass){
        List<Object> objects = redisTemplate.opsForValue().multiGet(Arrays.asList(keys.toArray()));
        if (Objects.isNull(objects)){
            return new ArrayList<>();
        }

        return objects.stream().map(obj -> toOneBean(obj, tClass)).collect(Collectors.toList());
    }

    public static void del(String key){
        redisTemplate.delete(key);
    }

    public static void del(Collection<String> key){
        redisTemplate.delete(key);
    }

    public static void sput(String key, Object value){
        redisTemplate.opsForSet().add(key, value);
    }

    public static <T> Set<T> sget(String key, Class<T> tClass){
        Set<Object> members = redisTemplate.opsForSet().members(key);
        if (Objects.isNull(members)){
            return new HashSet<>();
        }

        return members.stream().map(member -> toOneBean(member, tClass)).collect(Collectors.toSet());
    }

    static <T> T toOneBean(Object obj, Class<T> tClass){
        String jsonString = JSONObject.toJSONString(obj);

        return obj == null ? null : JSONObject.parseObject(jsonString, tClass);
    }

    public static void sdel(String cacheKey, Long userId) {
        redisTemplate.opsForSet().remove(cacheKey, userId);
    }

    public static boolean hasExisted(String cacheKey) {
        Object object = redisTemplate.opsForValue().get(cacheKey);
        return !Objects.isNull(object);
    }

    /**
     * hash相关操作
     */
    public static void hput(String hashKey, String key, Object value){
        redisTemplate.opsForHash().put(hashKey, key, value);
    }

    public static <T> T hget(String hashKey, String key, Class<T> tClass){
        Object obj = redisTemplate.opsForHash().get(hashKey, key);
        return toOneBean(obj, tClass);
    }

    public static <T> Map<String, T> hgetAll(String hashKey, Class<T> valueClass){
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(hashKey);
        Map<String, T> res = new HashMap<>();
        entries.forEach((key, value) -> {
            res.put((String) key, toOneBean(value, valueClass));
        });

        return res;
    }

    public static void hdel(String hashKey, String key){
        redisTemplate.opsForHash().delete(hashKey, key);
    }

    public static void hdel(String hashKey, Collection<String> keys){
        redisTemplate.opsForHash().delete(hashKey, keys.toArray());
    }
}
