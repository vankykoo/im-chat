package com.vanky.chat.leaf.utils;

import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.leaf.constant.RedisCacheKey;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author vanky
 * @create 2024/10/16 20:06
 */
@Component
@Slf4j
public class CachedIdUtil {

    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    private static final Long MAX_ID_SIZE = 10000L;

    private static final double THRESHOLD = 0.5;

    private String REDIS_CACHED_ID = RedisCacheKey.REDIS_CACHED_ID;

    /**
     * 获取id
     * @return
     */
    public Long getId(){
        Long idNum = RedisUtil.lLength(REDIS_CACHED_ID);

        if (idNum < MAX_ID_SIZE * THRESHOLD){
            // 缓存中的 id 数已经低于阈值，需要补充，异步
            new Thread(() -> fillCache(idNum)).start();
        }

        if (idNum == 0){
            // 如果缓存中没有id，先直接返回
            //log.info("缓存中没有id，先直接返回");
            return snowflakeIdWorker.nextId();
        }

        Long newId = RedisUtil.lget(REDIS_CACHED_ID, Long.class);

        //log.info("从缓存中获取 id ：{}", newId);

        return newId;
    }

    /**
     * 补充id
     */
    private void fillCache(Long idNum){
        Long need = MAX_ID_SIZE - idNum;

        log.info("缓存中 id 数位：{}，正在补充...", idNum);

        for (long i = 0; i < need; i++){
            RedisUtil.lput(REDIS_CACHED_ID, snowflakeIdWorker.nextId());
        }
    }

}
