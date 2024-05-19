package com.vanky.chat.client;

import com.vanky.chat.common.feign.groupfeign.GroupFeignClient;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.vanky.chat")
@EnableDiscoveryClient
@EnableFeignClients(clients = {IdGeneratorFeignClient.class, ImUserFeignClient.class, GroupFeignClient.class})
public class ClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class,args);
    }
}