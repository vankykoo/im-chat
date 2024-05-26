package com.vanky.chat.client.processor;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.google.protobuf.ByteString;
import com.vanky.chat.client.utils.MsgGenerator;
import com.vanky.chat.client.utils.SendAckMsgUtil;
import com.vanky.chat.client.utils.SendMsgUtil;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vanky
 * @create 2024/4/26 22:18
 */
@Component
@Slf4j
public class GroupMsgProcessor {

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    @Resource
    private MsgGenerator msgGenerator;

    public void receivedGroupMsg(BaseMsgProto.BaseMsg msg, Long currentClientUserId, Channel channel){
        try {
            BaseMsgProto.BaseMsg ackMsg = msgGenerator
                    .generateGroupAckMsg(msg.getToUserId(),
                            currentClientUserId,
                            msg.getUniqueId());

            //消息解码
            String rowContent = msgEncryptUtil
                    .msgDecrypt(msg.getContent(), msg.getToUserId(),
                            msg.getFromUserId(), TypeEnum.ChatType.GROUP_CHAT.getValue());

            log.info("用户{} 收到群消息: {}", currentClientUserId, rowContent);
            //ack
            SendMsgUtil.sendMsg((NioSocketChannel) channel, ackMsg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void receivedOfflineGroupMsgDetail(BaseMsgProto.BaseMsg msg, Channel channel){
        //全量离线消息
        //需要解密
        String jsonString = msgEncryptUtil.msgDecrypt(msg.getContent(), msg.getFromUserId(), msg.getToUserId(), msg.getChatType());

        log.info("收到群聊全量离线消息：{}",jsonString);

        //发送ack消息
        SendAckMsgUtil.sendAckMsg(msg, (NioSocketChannel) channel, TypeEnum.MsgType.OFFLINE_ACK_MSG.getValue());
    }

}
