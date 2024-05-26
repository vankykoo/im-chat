package com.vanky.chat.common.handler;

import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MultiProtocolDecoder extends ByteToMessageDecoder {
    private static final int PREFIX_LENGTH = 5;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableLen = in.readableBytes();
        if (readableLen < PREFIX_LENGTH) {
            return; // 不足以读取前缀
        }

        in.markReaderIndex(); // 标记当前读取位置
        //获取消息类型
        byte[] prefixBytes = new byte[PREFIX_LENGTH];
        in.readBytes(prefixBytes, 0 , 5);
        String prefix = new String(prefixBytes);

        if ("PROTO".equals(prefix)) {
            readableLen = in.readableBytes();
            byte[] bytes = new byte[readableLen];
            in.readBytes(bytes, 0, readableLen);
            //转为protobuf类型，写入out，交给后面的处理器处理
            BaseMsgProto.BaseMsg msg = BaseMsgProto.BaseMsg.parseFrom(bytes);
            out.add(msg);
        } else if ("STRNG".equals(prefix)) {
            readableLen = in.readableBytes();
            byte[] stringBytes = new byte[readableLen];
            in.readBytes(stringBytes, 0, readableLen);
            out.add(new String(stringBytes));
        } else {
            in.resetReaderIndex();
            throw new IllegalArgumentException("Unknown message prefix: " + prefix);
        }
    }
}
