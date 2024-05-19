package com.vanky.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.vanky.chat",exclude = DataSourceAutoConfiguration.class)
public class ImLeafApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImLeafApplication.class, args);
    }
}