package com.vanky.chat.common.feign.nettyclientfeign;

import com.vanky.chat.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author vanky
 * @create 2024/5/11 16:23
 */
@FeignClient(value = "im-client")
public interface NettyClientFeignClient {

    @GetMapping("/client/connect")
    Result connect(@RequestParam("userId") Long userId);

}
