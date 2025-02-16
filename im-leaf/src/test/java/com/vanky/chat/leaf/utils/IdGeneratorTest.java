package com.vanky.chat.leaf.utils;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author vanky
 * @create 2024/10/16 21:11
 */
@SpringBootTest
public class IdGeneratorTest {

    @Resource
    private CachedIdUtil cachedIdUtil;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Test
    public void testCacheId(){
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            Long l = cachedIdUtil.getId();
            System.out.println(i + "----" + l);
        }

        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }

    @Test
    public void testSnowFlakeId(){
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            long l = snowflakeIdWorker.nextId();
            System.out.println(i + "----" + l);
        }

        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }

}
