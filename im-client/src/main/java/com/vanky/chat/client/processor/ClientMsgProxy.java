package com.vanky.chat.client.processor;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.google.protobuf.ByteString;
import com.vanky.chat.client.utils.SendAckMsgUtil;
import com.vanky.chat.common.bo.OfflineMsgInfo;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author vanky
 * @create 2024/9/12 20:55
 */
@Component
@Slf4j
public class ClientMsgProxy {

    @Resource
    @Lazy
    private ForwardMsgProcessor forwardMsgProcessor;

    @Resource
    private GroupMsgProcessor groupMsgProcessor;

    @Resource
    private PrivateMsgProcessor privateMsgProcessor;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    @Resource
    private LoginMsgProcessor loginMsgProcessor;

    public void receiveSimpleMsg(NioSocketChannel nioSocketChannel, BaseMsgProto.BaseMsg msg, Long currentClientUserId){
        if (msg.getChatType() == TypeEnum.ChatType.GROUP_CHAT.getValue()){
            groupMsgProcessor.receivedGroupMsg(msg, currentClientUserId, nioSocketChannel);
        }else{
            privateMsgProcessor.receivePrivateMsg(msg, nioSocketChannel);
        }
    }

    /**
     * 接收到 ack 的消息
     * @param msg
     */
    public void receiveAckMsg(BaseMsgProto.BaseMsg msg){
        //ack 消息
        log.info("客户端收到服务端ack消息：{}", msg);
    }

    /**
     * 接受到需要转发的消息
     * @param nioSocketChannel
     * @param msg
     */
    public void receiveForwardMsg(NioSocketChannel nioSocketChannel, BaseMsgProto.BaseMsg msg){
        //发ack消息
        SendAckMsgUtil.sendAckMsg(msg, nioSocketChannel, TypeEnum.MsgType.ACK_MSG.getValue());
        //处理转发消息
        forwardMsgProcessor.forwardMsg(msg);
    }

    /**
     * 离线消息的基本信息
     * @param nioSocketChannel
     * @param msg
     */
    public void receiveOfflineMsgInfo(NioSocketChannel nioSocketChannel, BaseMsgProto.BaseMsg msg){
        //离线私信/群聊消息 的 信息
        //群聊：from：groupId  to：userId
        //收到消息需要解密
        String rowContentJsonString = msgEncryptUtil
                .msgDecrypt(msg.getContent(), msg.getFromUserId(),
                        msg.getToUserId(), msg.getChatType());

        OfflineMsgInfo offlineMsgInfo = OfflineMsgInfo.json2OfflineMsg(rowContentJsonString);
        if (msg.getChatType() == TypeEnum.ChatType.PRIVATE_CHAT.getValue()){
            //私聊的消息需要解密,因为服务端没有保存明文
            byte[] bytes = ByteString.copyFrom(offlineMsgInfo.getContent()).toByteArray();
            String rowContent = msgEncryptUtil.msgDecrypt(ByteString.copyFrom(bytes), msg.getFromUserId(),
                    msg.getToUserId(), TypeEnum.ChatType.PRIVATE_CHAT.getValue());
            log.info("私信解码最后一条消息是：{}", rowContent);
        }

        log.info("收到离线消息的基本信息：{}", offlineMsgInfo);

        //发送ack消息
        SendAckMsgUtil.sendAckMsg(msg, nioSocketChannel, TypeEnum.MsgType.SIMPLE_ACK_MSG.getValue());
    }

    /**
     * 收到离线私聊消息
     * @param nioSocketChannel
     * @param msg
     */
    public void receiveOfflinePrivateMsgDetail(NioSocketChannel nioSocketChannel, BaseMsgProto.BaseMsg msg){
        //全量私聊离线消息
        privateMsgProcessor.receiveOfflinePrivateMsgDetail(msg, nioSocketChannel);
    }

    /**
     * 收到离线群聊消息
     * @param nioSocketChannel
     * @param msg
     */
    public void receivedOfflineGroupMsgDetail(NioSocketChannel nioSocketChannel, BaseMsgProto.BaseMsg msg){
        //全量群聊离线消息
        groupMsgProcessor.receivedOfflineGroupMsgDetail(msg, nioSocketChannel);
    }

    /**
     * 已读消息
     * @param nioSocketChannel
     * @param msg
     */
    public void readMsg(NioSocketChannel nioSocketChannel, BaseMsgProto.BaseMsg msg){
        //发送ack消息
        SendAckMsgUtil.sendAckMsg(msg, nioSocketChannel, TypeEnum.MsgType.SIMPLE_ACK_MSG.getValue());

        List<Long> longList = JSONObject.parseObject(CommonConverter.byteString2String(msg.getContent()), new TypeReference<>() {});

        if (msg.getChatType() == 0){
            //私聊已读：
            log.info("私信已读消息id集：{}", longList);
        }else{
            //群聊已读
            log.info("群聊已读消息的最大id：{}", longList.get(0));
        }
    }

    public void receiveHistoryMsg(NioSocketChannel nioSocketChannel, BaseMsgProto.BaseMsg msg) {
        // 处理
        privateMsgProcessor.historyMsg(msg);

        // 发送ack消息
        SendAckMsgUtil.sendAckMsg(msg, nioSocketChannel, TypeEnum.MsgType.SIMPLE_ACK_MSG.getValue());
    }

    public void receiveOnlineFriendsList(NioSocketChannel nioSocketChannel, BaseMsgProto.BaseMsg msg) {
        // 打印
        loginMsgProcessor.receiveOnlineFriendsIdList(msg);

        // 发送ack消息
        SendAckMsgUtil.sendAckMsg(msg, nioSocketChannel, TypeEnum.MsgType.SIMPLE_ACK_MSG.getValue());
    }

    public void receiveFriendStatusChangeMsg(NioSocketChannel nioSocketChannel, BaseMsgProto.BaseMsg msg) {
        loginMsgProcessor.receiveFriendStatusChangeMsg(msg);

        // 发送ack
        SendAckMsgUtil.sendAckMsg(msg, nioSocketChannel, TypeEnum.MsgType.SIMPLE_ACK_MSG.getValue());
    }
}
