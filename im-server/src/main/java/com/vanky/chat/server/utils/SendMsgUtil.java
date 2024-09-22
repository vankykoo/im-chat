package com.vanky.chat.server.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonMsgGenerator;
import com.vanky.chat.server.session.ChatSessionMap;
import io.netty.channel.Channel;

/**
 * @author vanky
 * @create 2024/4/14 21:31
 */
public class SendMsgUtil {

    private static WaitAckUtil waitAckUtil;

    private static CommonMsgGenerator commonMsgGenerator;

    static {
        SendMsgUtil.waitAckUtil = SpringUtil.getBean("waitAckUtil", WaitAckUtil.class);
        SendMsgUtil.commonMsgGenerator = SpringUtil.getBean("commonMsgGenerator", CommonMsgGenerator.class);
    }

    /**
     * 发送需要ack的消息
     * @param channel
     * @param msg
     */
    public static void sendMsg4Ack(Channel channel, BaseMsgProto.BaseMsg msg){
        channel.writeAndFlush(msg);

        waitAckUtil.saveWaitingAckMsgDetail(msg, msg.getId());
    }

    /**
     * 发送需要ack的消息
     * @param baseMsg
     */
    public static void sendMsg4Ack(BaseMsgProto.BaseMsg baseMsg){
        ChatSessionMap.sendMessage(baseMsg.getToUserId(), baseMsg);

        waitAckUtil.saveWaitingAckMsgDetail(baseMsg, baseMsg.getId());
    }

    public static void sendMsg4Ack(BaseMsgProto.BaseMsg baseMsg, Long toUserId){
        ChatSessionMap.sendMessage(toUserId, baseMsg);

        waitAckUtil.saveWaitingAckMsgDetail(baseMsg, baseMsg.getId());
    }

    /**
     * 发送ack消息
     * @param channel
     * @param msg
     * @param ackType
     */
    public static void sendAckMsg(Channel channel, BaseMsgProto.BaseMsg msg, TypeEnum.MsgType ackType){
        BaseMsgProto.BaseMsg ackMsg = commonMsgGenerator.generateAckMsg(msg, ackType.getValue());

        channel.writeAndFlush(ackMsg);
    }

}
