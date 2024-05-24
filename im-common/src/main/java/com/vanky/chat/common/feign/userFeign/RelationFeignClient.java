package com.vanky.chat.common.feign.userFeign;

import com.vanky.chat.common.config.FeignRequestInterceptor;
import com.vanky.chat.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author vanky
 * @create 2024/5/21 17:14
 */
//@FeignClient(value = "im-user")
@FeignClient(name = "192.168.200.134:80", configuration = FeignRequestInterceptor.class)
public interface RelationFeignClient {

    /**
     * 判断用户是否为好友关系
     * @param fromUserId
     * @param toUserId
     * @return
     */
    @GetMapping("/user/relation/areUsersFriends")
    Result<Boolean> areUsersFriends(@RequestParam("fromUserId")Long fromUserId,
                                    @RequestParam("toUserId")Long toUserId);

}
