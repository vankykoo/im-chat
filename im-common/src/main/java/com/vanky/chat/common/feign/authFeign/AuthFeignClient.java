package com.vanky.chat.common.feign.authFeign;

import com.vanky.chat.common.bo.AccessTokenInfoBO;
import com.vanky.chat.common.bo.CheckRedisTokenBo;
import com.vanky.chat.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author vanky
 * @create 2024/5/16 20:06
 */
@FeignClient(value = "im-auth")
public interface AuthFeignClient {

    @GetMapping(value = "/auth/generateAccessToken")
    Result<AccessTokenInfoBO> generateAccessToken(@RequestParam("userId") Long userId);

    @GetMapping(value = "/auth/getTokenInfo")
    Result<AccessTokenInfoBO> getTokenInfo(@RequestParam("accessToken") String accessToken);

    @DeleteMapping(value = "/auth/deleteAllTokens")
    Result deleteAllTokens(@RequestParam("userId") Long userId);

    @PostMapping("/auth/checkAllTokens")
    Result checkAllTokens(@RequestBody CheckRedisTokenBo checkRedisTokenBo);
}
