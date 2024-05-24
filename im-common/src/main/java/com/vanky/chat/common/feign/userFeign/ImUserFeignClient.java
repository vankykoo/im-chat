package com.vanky.chat.common.feign.userFeign;

import com.vanky.chat.common.bo.ImUserBo;
import com.vanky.chat.common.config.FeignRequestInterceptor;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.to.UserTo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author vanky
 * @create 2024/4/23 15:01
 */
//@FeignClient(value = "im-user")
@FeignClient(name = "192.168.200.134:80", configuration = FeignRequestInterceptor.class)
public interface ImUserFeignClient {

    @GetMapping(value = "/user/getUserPublicKey")
    Result<String> getUserPublicKey(@RequestParam("userId") Long userId);

    @GetMapping(value = "/user/getUserById")
    Result<ImUserBo> getUserById(@RequestParam("userId") Long userId);

    @GetMapping(value = "/user/getUserByUserName")
    Result<ImUserBo> getUserByUserName(@RequestParam("username") String username);

    @PostMapping("/user/register")
    Result register(@RequestBody UserTo userTo);
}
