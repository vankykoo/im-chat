package com.vanky.chat.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

/**
 * @author vanky
 * @create 2024/5/13 20:32
 */
@Configuration
public class DefaultConfig {
    /**
     * 解决：
     * No qualifying bean of type 'org.springframework.boot.autoconfigure.http.HttpMessageConverters'
     * available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations:
     * {@org.springframework.beans.factory.annotation.Autowired(required=true)}
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }


    @Bean
    @ConditionalOnMissingBean
    public ServerCodecConfigurer serverCodecConfigurer(){
        return new DefaultServerCodecConfigurer();
    }
}
