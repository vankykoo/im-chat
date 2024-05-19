package com.vanky.chat.client.utils;

import com.vanky.chat.common.bo.GroupMsgBo;
import com.vanky.chat.common.bo.PrivateMsgBo;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author vanky
 * @create 2024/3/29 16:40
 */
@Component
public class MsgGenerator {
    @Resource
    private IdGeneratorFeignClient idGeneratorFeignClient;

    public BaseMsgProto.BaseMsg generateLoginMsg(Long userId){
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();

        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setCreateTime(System.currentTimeMillis())
                .setFromUserId(userId)
                .setMsgType(TypeEnum.MsgType.LOGIN_MSG.getValue());

        return builder.build();
    }

    public BaseMsgProto.BaseMsg generatePrivateMsg(PrivateMsgBo privateMsgBo){
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();

        //顺序id生成
        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setChatType(TypeEnum.ChatType.PRIVATE_CHAT.getValue())
                .setContent(privateMsgBo.getContent())
                //唯一id生成
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setFromUserId(privateMsgBo.getFromUserId())
                .setToUserId(privateMsgBo.getToUserId())
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(TypeEnum.MsgType.CHAT_MSG.getValue())
                .setStatus(TypeEnum.MsgStatus.NOT_SENT.getValue());

        return builder.build();
    }

    /**
     * 生成群消息
     * @param groupMsgBo
     * @return
     */
    public BaseMsgProto.BaseMsg generateGroupMsg(GroupMsgBo groupMsgBo){
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();

        //顺序id生成
        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setChatType(TypeEnum.ChatType.GROUP_CHAT.getValue())
                .setContent(groupMsgBo.getContent())
                //唯一id生成
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setFromUserId(groupMsgBo.getFromUserId())
                .setToUserId(groupMsgBo.getGroupId())
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(TypeEnum.MsgType.CHAT_MSG.getValue())
                .setStatus(TypeEnum.MsgStatus.NOT_SENT.getValue());

        return builder.build();
    }

    /**
     * 生成群消息ack消息
     * @param groupId 群id
     * @param userId 当前客户端的用户id
     * @param msgUniqueId 要ack的消息递增id
     * @return
     */
    public BaseMsgProto.BaseMsg generateGroupAckMsg(Long groupId, Long userId, Long msgUniqueId){
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();

        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setChatType(TypeEnum.ChatType.GROUP_CHAT.getValue())
                .setContent(CommonConverter.string2ByteString(msgUniqueId.toString()))
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setFromUserId(userId)
                .setToUserId(groupId)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(TypeEnum.MsgType.ACK_MSG.getValue())
                .setStatus(TypeEnum.MsgStatus.NOT_SENT.getValue());

        return builder.build();
    }

    /**
     * 通知服务端，客户端已经阅读了已抵达的消息
     * @param fromUserId
     * @param toUserId
     * @return
     */
    public BaseMsgProto.BaseMsg generateHasReadNoticeMsg(Long fromUserId, Long toUserId, TypeEnum.ChatType chatType) {
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();

        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setChatType(chatType.getValue())
                //.setContent(msgUniqueId.toString())
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setFromUserId(fromUserId)
                .setToUserId(toUserId)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(TypeEnum.MsgType.HAS_READ_MSG.getValue())
                .setStatus(TypeEnum.MsgStatus.NOT_SENT.getValue());

        return builder.build();

    }

    public BaseMsgProto.BaseMsg generateLogoutMsg(Long userId) {
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();

        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setCreateTime(System.currentTimeMillis())
                .setFromUserId(userId)
                .setMsgType(TypeEnum.MsgType.LOGOUT_MSG.getValue());

        return builder.build();
    }
}
