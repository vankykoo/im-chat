package com.vanky.chat.common.feign.groupfeign;

import com.vanky.chat.common.config.FeignRequestInterceptor;
import com.vanky.chat.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author vanky
 * @create 2024/4/23 21:28
 */
//@FeignClient(value = "im-server")
@FeignClient(name = "192.168.200.134:80", configuration = FeignRequestInterceptor.class)
public interface GroupFeignClient {
    @GetMapping("/server/group/getPublicKey")
    Result<String> getPublicKey(@RequestParam("groupId") Long groupId);
}
