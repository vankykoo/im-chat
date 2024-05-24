package com.vanky.chat.common.feign.leafFeign;

import com.vanky.chat.common.config.FeignRequestInterceptor;
import com.vanky.chat.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author vanky
 * @create 2024/4/13 20:58
 */
//@FeignClient(value = "im-leaf")
@FeignClient(name = "192.168.200.134:80", configuration = FeignRequestInterceptor.class)
public interface IdGeneratorFeignClient {

    @GetMapping(value = "/id/nextId")
    Result<Long> nextId();

}
