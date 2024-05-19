package com.vanky.chat.server.push;

import com.vanky.chat.common.bo.BaseMsgBo;
import com.vanky.chat.server.pojo.BaseMsg;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * 接收用户的推送请求，写入 kafka
 * @author vanky
 * @create 2024/4/17 20:19
 */
@Component
@Slf4j
public class PushProxy {

    @Resource
    private KafkaTemplate<String, BaseMsgBo> kafkaTemplate;

    //1.推送 私信请求
    public void privateMsg(BaseMsg baseMsg){
        BaseMsgBo baseMsgBo = new BaseMsgBo();
        BeanUtils.copyProperties(baseMsg, baseMsgBo);
        baseMsgBo.setContent(baseMsg.getContent());

        try {
            SendResult<String, BaseMsgBo> sendResult = kafkaTemplate
                    .send("privateMsg", "privateKey", baseMsgBo).get();

            log.info("PushProxy 成功存入【私信】推送请求到kafka ：{} --> {}",
                    sendResult.getProducerRecord().topic(), sendResult.getProducerRecord().value().toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    //2.推送 群聊请求
    public void groupMsg(BaseMsg baseMsg){
        BaseMsgBo baseMsgBo = new BaseMsgBo();
        BeanUtils.copyProperties(baseMsg, baseMsgBo);

        try {
            SendResult<String, BaseMsgBo> sendResult = kafkaTemplate
                    .send("groupMsg", "groupKey", baseMsgBo).get();

            log.info("PushProxy 成功存入【群聊】消息推送请求到kafka ：{} --> {}",
                    sendResult.getProducerRecord().topic(), sendResult.getProducerRecord().value().toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    //3.推送 离线消息 请求
    public void offlineMsg(BaseMsg baseMsg){
        int msgType = baseMsg.getMsgType();
        BaseMsgBo baseMsgBo = new BaseMsgBo();
        BeanUtils.copyProperties(baseMsg, baseMsgBo);

        try {
            String topic = "";
            switch (msgType){
                case 7:
                case 8:
                    //offlineMsgInfo
                    topic = "offlineMsgInfo";
                    break;
                case 9:
                    //离线私信
                    topic = "offlinePrivateMsg";
                    break;
                case 10:
                    //离线群聊
                    topic = "offlineGroupMsg";
                    break;
            }

            SendResult<String, BaseMsgBo> sendResult = kafkaTemplate
                    .send(topic, topic + "Key", baseMsgBo).get();
            log.info("PushProxy 成功存入【离线】消息推送请求到kafka ：{} --> {}",
                    sendResult.getProducerRecord().topic(), sendResult.getProducerRecord().value().toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
