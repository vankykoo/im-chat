package com.vanky.chat.client.netty;

import com.vanky.chat.common.bo.ImUserBo;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import com.vanky.chat.common.response.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author vanky
 * @create 2024/10/28 21:20
 */
@SpringBootTest
public class UserLoginTest {

    @Resource
    private ImUserFeignClient imUserFeignClient;

    @Resource
    private NettyClient nettyClient;

    @Test
    public void userLoginTest1(){
        for (int i = 0; i < 100; i++) {
            Result<ImUserBo> result = imUserFeignClient.getUserByUserName("uu-" + i);
            ImUserBo userBo = result.getData();

            nettyClient.connect(null, null, userBo.getUserId());
        }
    }

    @Test
    public void connectToServerTest(){
        for (int i = 0; i < 100; i++) {

        }
    }

}
