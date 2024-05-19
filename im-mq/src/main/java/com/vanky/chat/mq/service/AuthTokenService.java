package com.vanky.chat.mq.service;

import com.rabbitmq.client.Channel;
import com.vanky.chat.common.bo.CheckRedisTokenBo;
import com.vanky.chat.common.bo.RefreshTokenInfoBo;
import com.vanky.chat.common.cache.RedisSimpleCacheName;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.feign.authFeign.AuthFeignClient;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author vanky
 * @create 2024/5/18 18:36
 */
@Component
@Slf4j
public class AuthTokenService {

    @Resource
    private AuthFeignClient authFeignClient;

    /**
     *
     * @param message
     * @param channel
     * @param userId
     */
    @RabbitListener(queues = {"auth.token.check.queue"})
    public void authTokenCheckListener(Message message, Channel channel, Long userId){
        String accessTokensKey = RedisSimpleCacheName.ACCESS_TOKEN + userId;
        Map<String, RefreshTokenInfoBo> map = RedisUtil.hgetAll(accessTokensKey, RefreshTokenInfoBo.class);

        CheckRedisTokenBo checkRedisTokenBo = new CheckRedisTokenBo(accessTokensKey, map);
        Result result = authFeignClient.checkAllTokens(checkRedisTokenBo);

        if (!result.isSuccess()){
            throw new MyException.FeignProcessException();
        }

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
