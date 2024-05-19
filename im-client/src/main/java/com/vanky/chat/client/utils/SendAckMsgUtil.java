package com.vanky.chat.client.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonMsgGenerator;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author vanky
 * @create 2024/4/12 22:01
 */
public class SendAckMsgUtil {

    private static CommonMsgGenerator commonMsgGenerator;

    static {
        SendAckMsgUtil.commonMsgGenerator = SpringUtil
                .getBean("commonMsgGenerator", CommonMsgGenerator.class);
    }

    public static void sendAckMsg(BaseMsgProto.BaseMsg msg, NioSocketChannel channel, int msgType){
        BaseMsgProto.BaseMsg ackMsg = commonMsgGenerator.generateAckMsg(msg, msgType);
        channel.writeAndFlush(ackMsg);
    }

}
