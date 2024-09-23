package com.vanky.chat.server.utils;

import com.alibaba.fastjson2.JSONObject;
import com.google.protobuf.ByteString;
import com.vanky.chat.common.bo.OfflineGroupMsgDetailBo;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.bo.OfflineMsgInfo;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import com.vanky.chat.server.pojo.BaseMsg;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author vanky
 * @create 2024/3/29 16:40
 */
@Component
public class MsgGenerator {

    @Resource
    private IdGeneratorFeignClient idGeneratorFeignClient;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    /**
     * 生成拉取通知消息，通知客户端来拉取
     * @param msg
     * @return
     */
    public BaseMsgProto.BaseMsg generatePullNoticeMsg(BaseMsgProto.BaseMsg msg){
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();

        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setChatType(TypeEnum.ChatType.GROUP_CHAT.getValue())
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setToUserId(msg.getToUserId()) //群id
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(TypeEnum.MsgType.PULL_NOTICE_MSG.getValue())
                .setStatus(TypeEnum.MsgStatus.NOT_SENT.getValue());

        return builder.build();
    }

    public BaseMsgProto.BaseMsg generateOnlineListMsg(Long userId, List<Long> friendsIdList){
        String jsonString = JSONObject.toJSONString(friendsIdList);

        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();
        builder.setId(idGeneratorFeignClient.nextId().getData())
                //.setChatType(chatType.getValue())
                .setContent(CommonConverter.string2ByteString(jsonString))
                //.setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setFromUserId(userId)
                //.setToUserId(toUserId)
                .setCreateTime(new Date().getTime())
                .setMsgType(TypeEnum.MsgType.ONLINE_FRIEND_LIST_MSG.getValue());

        return builder.build();
    }

    /**
     * 生成离线私信消息
     * @param offlineMsgInfo
     * @return
     */
    public BaseMsg generateOfflinePrivateMsg(OfflineMsgInfo offlineMsgInfo, Long toUserId){
        String jsonString = JSONObject.toJSONString(offlineMsgInfo);

        //把这个信息进行加密
        ByteString byteString = msgEncryptUtil
                .msgEncrypt(jsonString, offlineMsgInfo.getFromUserId(),
                        toUserId, TypeEnum.ChatType.PRIVATE_CHAT.getValue());

        return BaseMsg.builder()
                .id(idGeneratorFeignClient.nextId().getData())
                .chatType(TypeEnum.ChatType.PRIVATE_CHAT.getValue())
                .content(byteString.toByteArray())
                .fromUserId(offlineMsgInfo.getFromUserId())
                .toUserId(toUserId)
                .uniqueId(idGeneratorFeignClient.nextId().getData())
                .createTime(new Date())
                .msgType(TypeEnum.MsgType.OFFLINE_PRIVATE_MSG_INFO.getValue())
                .status(TypeEnum.MsgStatus.NOT_SENT.getValue())
                .build();
    }

    /**
     * 生成离线群聊消息
     * @param offlineMsgInfo
     * @return
     */
    public BaseMsg generateOfflineGroupMsg(OfflineMsgInfo offlineMsgInfo, Long toUserId){
        String jsonString = JSONObject.toJSONString(offlineMsgInfo);

        ByteString byteStringContent = msgEncryptUtil.msgEncrypt(jsonString,
                offlineMsgInfo.getFromUserId(), toUserId, TypeEnum.ChatType.GROUP_CHAT.getValue());

        //顺序id生成
        BaseMsg baseMsg = BaseMsg.builder()
                .id(idGeneratorFeignClient.nextId().getData())
                .chatType(TypeEnum.ChatType.GROUP_CHAT.getValue())
                .content(byteStringContent.toByteArray())
                .fromUserId(offlineMsgInfo.getFromUserId())
                .toUserId(toUserId)
                .uniqueId(idGeneratorFeignClient.nextId().getData())
                .createTime(new Date(System.currentTimeMillis()))
                .msgType(TypeEnum.MsgType.OFFLINE_GROUP_MSG_INFO.getValue())
                .status(TypeEnum.MsgStatus.NOT_SENT.getValue())
                .build();

        return baseMsg;
    }


    /**
     * 封装一个聊天框的全量离线消息
     * @param offlineMsgList
     * @param fromUserId
     * @param toUserId
     * @return
     */
    public BaseMsg generateOfflinePrivateMsgDetail(List<OfflineMsgDetailBo> offlineMsgList,
                                                                          Long fromUserId,
                                                                          Long toUserId) {
        String jsonString = JSONObject.toJSONString(offlineMsgList);

        //消息加密
        ByteString byteString = msgEncryptUtil.msgEncrypt(jsonString, fromUserId,
                toUserId, TypeEnum.ChatType.PRIVATE_CHAT.getValue());

        return BaseMsg.builder()
                .id(idGeneratorFeignClient.nextId().getData())
                .chatType(TypeEnum.ChatType.PRIVATE_CHAT.getValue())
                .content(byteString.toByteArray())
                .uniqueId(idGeneratorFeignClient.nextId().getData())
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .createTime(new Date())
                .msgType(TypeEnum.MsgType.OFFLINE_PRIVATE_MSG_DETAIL.getValue())
                .status(TypeEnum.MsgStatus.HAS_NOT_READ.getValue())
                .build();
    }



    /**
     * 把全量离线群聊消息进行分装，分页发送
     * @param offlineMsgs
     * @param groupId
     * @param toUserId
     * @return
     */
    public BaseMsg generateOfflineGroupMsgDetail(List<OfflineGroupMsgDetailBo> offlineMsgs,
                                                                    Long groupId,
                                                                    Long toUserId) {
        String jsonString = JSONObject.toJSONString(offlineMsgs);

        //加密
        ByteString byteString = msgEncryptUtil
                .msgEncrypt(jsonString, groupId, toUserId, TypeEnum.ChatType.GROUP_CHAT.getValue());

        return BaseMsg.builder()
                .id(idGeneratorFeignClient.nextId().getData())
                .chatType(TypeEnum.ChatType.GROUP_CHAT.getValue())
                .content(byteString.toByteArray())
                .uniqueId(idGeneratorFeignClient.nextId().getData())
                .fromUserId(groupId)
                .toUserId(toUserId)
                .createTime(new Date())
                .msgType(TypeEnum.MsgType.OFFLINE_GROUP_MSG_DETAIL.getValue())
                .status(TypeEnum.MsgStatus.HAS_NOT_READ.getValue())
                .build();
    }

    /**
     * 返回给客户端已读消息的uniqueId
     * @param hasReadMsgIds
     * @param fromUserId
     * @param toUserId
     * @param chatType
     * @return
     */
    public BaseMsgProto.BaseMsg generateHasReadMsgUniqueIdBaseMsg(List<Long> hasReadMsgIds,
                                                                  long fromUserId,
                                                                  long toUserId,
                                                                  TypeEnum.ChatType chatType) {
        String jsonString = JSONObject.toJSONString(hasReadMsgIds);

        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();
        builder.setId(idGeneratorFeignClient.nextId().getData())
                .setChatType(chatType.getValue())
                .setContent(CommonConverter.string2ByteString(jsonString))
                .setUniqueId(idGeneratorFeignClient.nextId().getData())
                .setFromUserId(fromUserId)
                .setToUserId(toUserId)
                .setCreateTime(new Date().getTime())
                .setMsgType(TypeEnum.MsgType.HAS_READ_MSG.getValue());

        return builder.build();
    }

    public BaseMsg generateGroupMsg2Push(BaseMsgProto.BaseMsg msg, Long toUserId){
        BaseMsg baseMsg = BaseMsg.builder()
                .id(idGeneratorFeignClient.nextId().getData())
                .uniqueId(idGeneratorFeignClient.nextId().getData())
                .msgType(TypeEnum.MsgType.CHAT_MSG.getValue())
                .content(Long.toString(msg.getUniqueId()).getBytes())
                .createTime(new Date())
                .fromUserId(msg.getToUserId())  //群聊
                .toUserId(toUserId)     //接收方
                .chatType(TypeEnum.ChatType.GROUP_CHAT.getValue())
                .status(TypeEnum.MsgStatus.NOT_SENT.getValue())
                .build();

        return baseMsg;
    }

    /**
     * 生成转发消息
     * @param address
     * @param baseMsg
     * @return
     */
    public BaseMsg generateForwardMsg(String address, BaseMsg baseMsg, Long toUserId) {
        BaseMsg.builder()
                .uniqueId(idGeneratorFeignClient.nextId().getData())
                .toUserId(toUserId)
                .chatType(baseMsg.getChatType())
                .content((address + "_" + baseMsg.getUniqueId()).getBytes())
                .msgType(TypeEnum.MsgType.FORWARD_MSG.getValue())
                .createTime(new Date())
                .build();
        return null;
    }

    /**
     * 生成历史消息
     * @param historyMsgList
     * @param fromUserId
     * @param toUserId
     * @return
     */
    public BaseMsg generateHistoryMsg(List<BaseMsg> historyMsgList, Long fromUserId, Long toUserId) {
        String jsonString = JSONObject.toJSONString(historyMsgList);

        // 不用加密，用户获取到消息后自己解密，因为只有用户才有共享密钥

        return BaseMsg.builder()
                .id(idGeneratorFeignClient.nextId().getData())
                .chatType(TypeEnum.ChatType.PRIVATE_CHAT.getValue())
                .content(jsonString.getBytes())
                //.uniqueId(idGeneratorFeignClient.nextId().getData())
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .createTime(new Date())
                .msgType(TypeEnum.MsgType.HISTORY_MSG.getValue())
                .status(TypeEnum.MsgStatus.HAS_NOT_READ.getValue())
                .build();
    }

    /**
     * 生成用户状态改变通知消息
     * @param fromUserId
     * @param toUserId
     * @param status
     * @return
     */
    public BaseMsg generateUserStatusChangeMsg(Long fromUserId, Long toUserId, int status) {
        return BaseMsg.builder()
                .id(idGeneratorFeignClient.nextId().getData())
                .chatType(TypeEnum.ChatType.OTHER_CHAT.getValue())
                .content(Integer.toString(status).getBytes())
                //.uniqueId(idGeneratorFeignClient.nextId().getData())
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .createTime(new Date())
                .msgType(TypeEnum.MsgType.USER_STATUS_CHANGE.getValue())
                .status(TypeEnum.MsgStatus.NOT_SENT.getValue())
                .build();
    }
}
