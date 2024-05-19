package com.vanky.chat;

import com.vanky.chat.common.feign.authFeign.AuthFeignClient;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.vanky.chat")
@EnableFeignClients(clients = {IdGeneratorFeignClient.class, AuthFeignClient.class})
public class MqApplication {
    public static void main(String[] args) {
        SpringApplication.run(MqApplication.class, args);
    }
}