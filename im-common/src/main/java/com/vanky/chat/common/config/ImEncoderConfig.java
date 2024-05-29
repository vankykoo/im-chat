package com.vanky.chat.common.config;

import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vanky
 * @create 2024/5/29 22:08
 */
@Configuration
public class ImEncoderConfig {

    @Bean
    public ProtobufVarint32LengthFieldPrepender protobufVarint32LengthFieldPrepender(){
        return new ProtobufVarint32LengthFieldPrepender();
    }

    @Bean
    public ProtobufEncoder protobufEncoder(){
        return new ProtobufEncoder();
    }

    @Bean
    public StringEncoder stringEncoder(){
        return new StringEncoder();
    }
}
