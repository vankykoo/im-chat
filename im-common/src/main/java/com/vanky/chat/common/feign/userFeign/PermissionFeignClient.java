package com.vanky.chat.common.feign.userFeign;

import com.vanky.chat.common.bo.PermissionBo;
import com.vanky.chat.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author vanky
 * @create 2024/5/10 19:28
 */
@FeignClient(value = "im-user")
public interface PermissionFeignClient {

    @GetMapping("/user/permission/path")
    Result<PermissionBo> getPathRequestPermission(@RequestParam("uri") String uri);
}
