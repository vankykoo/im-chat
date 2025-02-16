package com.vanky.chat.user.service;

import com.vanky.chat.user.pojo.to.UserTo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author vanky
 * @create 2024/10/28 21:06
 */
@SpringBootTest
public class TestRegister {

    @Resource
    private ImUserService imUserService;

    @Test
    public void testUserRegister(){
        for (int i = 0; i < 100; i++){
            UserTo userTo = new UserTo();
            userTo.setUsername("uu-" + i);
            userTo.setPassword("123456");

            imUserService.register(userTo);
        }
    }

}
