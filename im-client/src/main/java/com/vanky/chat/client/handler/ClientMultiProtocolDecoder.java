package com.vanky.chat.client.handler;

import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class ClientMultiProtocolDecoder extends ByteToMessageDecoder {

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

    /**
     * 判断给定的 ByteBuf 是否包含 Protobuf 消息。
     *
     * @param byteBuf 要检查的 ByteBuf
     * @return 如果是 Protobuf 消息返回 true，否则返回 false
     */
    private boolean isProtobuf(ByteBuf byteBuf) {
        // 标记当前的读索引位置，以便后续重置
        byteBuf.markReaderIndex();
        boolean isProtobuf = false;

        try {
            // 尝试读取 Protobuf 消息的长度字段
            int length = readRawVarint32(byteBuf);
            // 判断长度是否合法，且剩余可读字节数是否足够长
            // 假设长度不为负且剩余字节足够长即为 Protobuf 消息
            isProtobuf = length >= 0 && byteBuf.readableBytes() >= length;
        } catch (Exception e) {
            // 如果出现异常，则认为不是 Protobuf 消息
            isProtobuf = false;
        } finally {
            // 重置读索引到标记的位置
            byteBuf.resetReaderIndex();
        }

        return isProtobuf;
    }

    /**
     * 从给定的 ByteBuf 中读取一个 varint32 编码的整数。
     *
     * @param byteBuf 要读取的 ByteBuf
     * @return 读取到的整数
     */
    private int readRawVarint32(ByteBuf byteBuf) {
        int shift = 0; // 位移量，用于计算结果
        int result = 0; // 最终结果

        while (shift < 32) { // varint32 的最大位移量为 32
            final byte b = byteBuf.readByte(); // 读取一个字节
            result |= (b & 0x7F) << shift; // 取出低 7 位并左移相应位数，合并到结果中
            if ((b & 0x80) == 0) { // 如果最高位为 0，说明这是最后一个字节
                return result; // 返回结果
            }
            shift += 7; // 否则继续处理下一个字节，位移量增加 7
        }

        // 如果超过了 32 位，说明 varint32 编码不合法
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
