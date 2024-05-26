package com.vanky.chat.client.utils;

import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author vanky
 * @create 2024/5/26 12:04
 */
public class SendMsgUtil {

    public static void sendMsg(NioSocketChannel channel, BaseMsgProto.BaseMsg msg){
        //ByteBuf buffer = Unpooled.buffer();
        //buffer.writeBytes("PROTO".getBytes());
        //buffer.writeBytes(msg.toByteArray());
        //channel.writeAndFlush(buffer);

        channel.writeAndFlush(msg);
    }

    public static void sendMsg(NioSocketChannel channel, String strMsg){
        //ByteBuf buffer = Unpooled.buffer();
        //buffer.writeBytes("STRNG".getBytes());
        //buffer.writeBytes(strMsg.getBytes());
        //channel.writeAndFlush(buffer);

        channel.writeAndFlush(strMsg);
    }

}
