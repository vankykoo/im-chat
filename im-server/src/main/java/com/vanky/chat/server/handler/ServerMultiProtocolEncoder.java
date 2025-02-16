package com.vanky.chat.server.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 通过检查消息类型，在Netty的ChannelPipeline中动态添加或删除不同的编码器
 */
@Slf4j
public class ServerMultiProtocolEncoder extends MessageToMessageEncoder<Object> {

    // protobuf 的编码器
    public static final ProtobufEncoder protobufEncoder;
    // protobuf 的长度控制器
    public static final ProtobufVarint32LengthFieldPrepender protobufVarint32LengthFieldPrepender;
    // String 的编码器
    public static final StringEncoder stringEncoder;

    /**
     * 注入资源
     */
    static {
        protobufEncoder = SpringUtil.getBean("protobufEncoder");
        protobufVarint32LengthFieldPrepender = SpringUtil.getBean("protobufVarint32LengthFieldPrepender");
        stringEncoder = SpringUtil.getBean("stringEncoder");
    }

    /**
     * 编码
     * @param ctx           the {@link ChannelHandlerContext} which this {@link MessageToMessageEncoder} belongs to
     * @param msg           the message to encode to an other one
     * @param out           the {@link List} into which the encoded msg should be added
     *                      needs to do some kind of aggregation
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        // 获取 pipeline 中的 handler 集合
        Map<String, ChannelHandler> map = ctx.pipeline().toMap();

        if (msg instanceof BaseMsgProto.BaseMsg) {
            // 如果是 protobuf 类型的消息
            if (map.containsKey("stringEncoder")){
                // 如果有 String 类型的编码器，需要移除
                ctx.pipeline().remove("stringEncoder");
            }

            // 如果没有 protobuf 的编码器，需要加入
            if (!map.containsKey("protobufEncoder")){
                ctx.pipeline().addBefore("serverMultiProtocolEncoder", "protobufVarint32LengthFieldPrepender",
                        protobufVarint32LengthFieldPrepender);
                ctx.pipeline().addBefore("serverMultiProtocolEncoder", "protobufEncoder",
                        protobufEncoder);
            }

            out.add(msg);
        } else if (msg instanceof String) {
            if (map.containsKey("protobufEncoder")){
                ctx.pipeline().remove("protobufEncoder");
                ctx.pipeline().remove("protobufVarint32LengthFieldPrepender");
            }

            if (!map.containsKey("stringEncoder")){
                ctx.pipeline().addBefore("serverMultiProtocolEncoder", "stringEncoder",
                        stringEncoder);
            }

            out.add(msg);
        }
    }
}
