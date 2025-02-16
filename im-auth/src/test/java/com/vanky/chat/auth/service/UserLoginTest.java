package com.vanky.chat.auth.service;

import com.vanky.chat.common.bo.ImUserBo;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.to.UserTo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author vanky
 * @create 2024/10/28 21:26
 */
@SpringBootTest
public class UserLoginTest {

    @Resource
    private ImUserFeignClient imUserFeignClient;

    @Resource
    private AuthService authService;

    @Test
    public void userLoginTest1(){
        for (int i = 0; i < 10; i++) {
            UserTo userTo = new UserTo("uu-" + i, "123456");

            authService.userLogin(userTo);
        }
    }

}
