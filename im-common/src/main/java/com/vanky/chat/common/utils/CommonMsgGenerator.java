package com.vanky.chat.common.utils;

import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author vanky
 * @create 2024/3/30 16:13
 */
@Component
public class CommonMsgGenerator {

    @Resource
    @Lazy
    private IdGeneratorFeignClient idGeneratorFeignClient;

    public BaseMsgProto.BaseMsg generateForwardMsg(String address){
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();

        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setChatType(TypeEnum.ChatType.PRIVATE_CHAT.getValue())
                .setContent(CommonConverter.string2ByteString(address))
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(TypeEnum.MsgType.FORWARD_MSG.getValue())
                .setStatus(TypeEnum.MsgStatus.SENT.getValue());

        return builder.build();
    }

    public BaseMsgProto.BaseMsg generateAckMsg(BaseMsgProto.BaseMsg msg, int msgType){
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();

        //私信ack消息
        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setChatType(msg.getChatType())
                .setContent(CommonConverter.string2ByteString(Long.toString(msg.getUniqueId())))
                .setFromUserId(msg.getFromUserId())
                .setToUserId(msg.getToUserId())
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(msgType)
                .setStatus(TypeEnum.MsgStatus.SENT.getValue());

        return builder.build();
    }

}
