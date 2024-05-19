package com.vanky.chat.user;

import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.vanky.chat")
@MapperScan("com.vanky.chat.user.mapper")
@EnableDiscoveryClient
@EnableFeignClients(clients = {IdGeneratorFeignClient.class})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}