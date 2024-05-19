package com.vanky.chat.common.feign.leafFeign;

import com.vanky.chat.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author vanky
 * @create 2024/4/13 20:58
 */
@FeignClient(value = "im-leaf")
public interface IdGeneratorFeignClient {

    @GetMapping(value = "/id/nextId")
    Result<Long> nextId();

}
