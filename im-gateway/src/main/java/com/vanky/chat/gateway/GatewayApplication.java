package com.vanky.chat.gateway;

import com.vanky.chat.common.feign.authFeign.AuthFeignClient;
import com.vanky.chat.common.feign.nettyclientfeign.NettyClientFeignClient;
import com.vanky.chat.common.feign.userFeign.PermissionFeignClient;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.vanky.chat"},exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(clients = {ImUserFeignClient.class, NettyClientFeignClient.class, PermissionFeignClient.class, AuthFeignClient.class})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}