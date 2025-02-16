package com.vanky.chat.server.service;

import com.vanky.chat.common.bo.ImUserBo;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import com.vanky.chat.common.response.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author vanky
 * @create 2024/10/28 21:11
 */
@SpringBootTest
public class JoinGroupTest {

    @Resource
    private GroupUserService groupUserService;

    @Resource
    private ImUserFeignClient imUserFeignClient;

    @Test
    public void joinGroupTest1(){
        for (int i = 0; i < 100; i++) {
            Result<ImUserBo> result = imUserFeignClient.getUserByUserName("uu-" + i);
            Long userId = result.getData().getUserId();

            groupUserService.joinGroup(userId, 21L);
        }
    }

}
