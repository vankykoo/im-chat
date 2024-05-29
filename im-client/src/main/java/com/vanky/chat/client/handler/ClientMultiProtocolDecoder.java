package com.vanky.chat.client.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
public class ClientMultiProtocolDecoder extends ByteToMessageDecoder {

    //public static final ProtobufDecoder protobufDecoder;
    //
    //public static final ProtobufVarint32FrameDecoder protobufVarint32FrameDecoder;
    //
    //public static final StringDecoder stringDecoder;
    //
    //static {
    //    protobufDecoder = SpringUtil.getBean("protobufDecoder");
    //    protobufVarint32FrameDecoder = SpringUtil.getBean("protobufVarint32FrameDecoder");
    //    stringDecoder = SpringUtil.getBean("stringDecoder");
    //}

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        Map<String, ChannelHandler> map = ctx.pipeline().toMap();
        if (isProtobuf(in)) {
            if (map.containsKey("stringDecoder")){
                ctx.pipeline().remove("stringDecoder");
            }

            if (!map.containsKey("protobufDecoder")){
                //添加protobuf解码器
                ctx.pipeline().addAfter("clientMultiProtocolDecoder", "protobufDecoder",
                        new ProtobufDecoder(BaseMsgProto.BaseMsg.getDefaultInstance()));
                ctx.pipeline().addAfter("clientMultiProtocolDecoder", "protobufVarint32FrameDecoder",
                        new ProtobufVarint32FrameDecoder());
            }

            out.add(in.retain());
        } else if (isString(in)) {
            if (map.containsKey("protobufDecoder")){
                ctx.pipeline().remove("protobufDecoder");
                ctx.pipeline().remove("protobufVarint32FrameDecoder");
            }

            if (!map.containsKey("stringDecoder")){
                //添加 string 解码器
                ctx.pipeline().addBefore("clientMultiProtocolHandler", "stringDecoder",
                        new StringDecoder());
            }

            out.add(in.retain());
        } else {
            in.resetReaderIndex();
            throw new IllegalArgumentException("Unknown message prefix");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String message = cause.getMessage();
        if (message.contains("did not read anything but decoded a message")) return;
        super.exceptionCaught(ctx, cause);
    }

    private boolean isProtobuf(ByteBuf byteBuf) {
        // 检查消息的前几个字节，判断是否是 Protobuf 消息
        byteBuf.markReaderIndex(); // 标记读索引
        boolean isProtobuf = false;

        try {
            int length = readRawVarint32(byteBuf);
            // 假设长度不为负且剩余字节足够长即为Protobuf消息
            isProtobuf = length >= 0 && byteBuf.readableBytes() >= length;
        } catch (Exception e) {
            isProtobuf = false;
        } finally {
            byteBuf.resetReaderIndex(); // 重置读索引
        }

        return isProtobuf;
    }

    private int readRawVarint32(ByteBuf byteBuf) {
        int shift = 0;
        int result = 0;

        while (shift < 32) {
            final byte b = byteBuf.readByte();
            result |= (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                return result;
            }
            shift += 7;
        }
        throw new IllegalArgumentException("Varint32 too long");
    }

    private boolean isString(ByteBuf byteBuf) {
        // 检查消息是否为 UTF-8 编码的字符串
        byteBuf.markReaderIndex(); // 标记读索引
        boolean isString = false;

        try {
            String msg = byteBuf.toString(CharsetUtil.UTF_8);
            isString = msg.chars().allMatch(c -> c < 128); // 简单检查是否为 ASCII 字符
        } catch (Exception e) {
            isString = false;
        } finally {
            byteBuf.resetReaderIndex(); // 重置读索引
        }

        return isString;
    }
}
