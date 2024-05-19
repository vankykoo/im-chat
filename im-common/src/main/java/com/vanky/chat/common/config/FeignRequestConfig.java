package com.vanky.chat.common.config;

import com.vanky.chat.common.interceptor.FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vanky
 * @create 2024/5/1 13:40
 */
//@Configuration
public class FeignRequestConfig {

    //@Bean
    public FeignRequestInterceptor feignRequestInterceptor(){
        return new FeignRequestInterceptor();
    }

}
