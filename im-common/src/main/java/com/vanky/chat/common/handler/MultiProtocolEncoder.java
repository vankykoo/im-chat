package com.vanky.chat.common.handler;

import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.Charset;
import java.util.List;

public class MultiProtocolEncoder extends MessageToMessageEncoder<Object> {
    private static final String PROTO_PREFIX = "PROTO";
    private static final String STRING_PREFIX = "STRNG";

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        if (msg instanceof BaseMsgProto.BaseMsg) {
            //消息前五位加上消息类型标识
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeBytes(PROTO_PREFIX.getBytes(), 0, 5);
            //原消息内容
            byte[] bytes = ((BaseMsgProto.BaseMsg) msg).toByteArray();
            buffer.writeBytes(bytes);
            out.add(buffer);
        } else if (msg instanceof String) {
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeBytes(STRING_PREFIX.getBytes(), 0, 5);
            byte[] bytes = ((String) msg).getBytes(Charset.forName("UTF-8"));
            buffer.writeBytes(bytes);
            out.add(buffer);
        }
    }
}
