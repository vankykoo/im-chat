package com.vanky.chat.auth;

import com.vanky.chat.common.feign.nettyclientfeign.NettyClientFeignClient;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.vanky.chat"})
@EnableDiscoveryClient
@EnableFeignClients(clients = {ImUserFeignClient.class, NettyClientFeignClient.class})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}