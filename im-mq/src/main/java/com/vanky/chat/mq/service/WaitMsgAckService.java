package com.vanky.chat.mq.service;

import com.rabbitmq.client.Channel;
import com.vanky.chat.common.bo.BaseMsgBo;
import com.vanky.chat.common.cache.WaitingAckCache;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author vanky
 * @create 2024/4/16 22:10
 */
@Component
@Slf4j
public class WaitMsgAckService {

    @Resource
    private KafkaTemplate<String, BaseMsgBo> kafkaTemplate;

    @RabbitListener(queues = {"msg.delay.queue"})
    public void WaitMsgAckDelayListener(Message message, Channel channel, String uniqueIdAndTimes){
        log.info("消息进入死信队列：{}", uniqueIdAndTimes);

        //uniqueId : times
        String[] split = uniqueIdAndTimes.split(":");
        Long uniqueId = Long.parseLong(split[0]);

        //1.到redis中查看 detail 是否存在
        String cacheKey = WaitingAckCache.WAITING_ACK_DETAIL_MSG + uniqueId;
        BaseMsgBo baseMsgBo = RedisUtil.get(cacheKey, BaseMsgBo.class);

        if (baseMsgBo != null){
            //2.如果存在，说明还没 收到/处理 ack
            int retryTime = Integer.parseInt(split[1]);
            if (retryTime == 1){
                throw new MyException.MessageSendException("重试发送多次消息失败！");
            }else {
                //重发
                resendMsg(baseMsgBo);
            }
        }
        //3.如果不存在，说明已经发送成功

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resendMsg(BaseMsgBo baseMsgBo){
        int msgType = baseMsgBo.getMsgType();
        int chatType = baseMsgBo.getChatType();

        if (msgType == TypeEnum.MsgType.CHAT_MSG.getValue()){
            if (chatType == TypeEnum.ChatType.PRIVATE_CHAT.getValue()){
                //私聊
                try {
                    SendResult<String, BaseMsgBo> sendResult = kafkaTemplate.send("privateMsg", "privateKey", baseMsgBo).get();

                    log.info("PushProxy 成功存入【重试 私信】推送请求到kafka ：{} --> {}",
                            sendResult.getProducerRecord().topic(), sendResult.getProducerRecord().value().toString());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }else{
                //群聊
                try {
                    SendResult<String, BaseMsgBo> sendResult = kafkaTemplate.send("groupMsg", "groupKey", baseMsgBo).get();

                    log.info("PushProxy 成功存入【重试 群聊】消息推送请求到kafka ：{} --> {}",
                            sendResult.getProducerRecord().topic(), sendResult.getProducerRecord().value().toString());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            //离线消息
            try {
                SendResult<String, BaseMsgBo> sendResult = kafkaTemplate.send("offlineMsg", "offlineKey", baseMsgBo).get();

                log.info("PushProxy 成功存入【重试 离线】消息推送请求到kafka ：{} --> {}",
                        sendResult.getProducerRecord().topic(), sendResult.getProducerRecord().value().toString());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
