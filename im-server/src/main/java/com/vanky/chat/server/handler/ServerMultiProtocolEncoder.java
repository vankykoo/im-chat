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

@Slf4j
public class ServerMultiProtocolEncoder extends MessageToMessageEncoder<Object> {

    public static final ProtobufEncoder protobufEncoder;
    public static final ProtobufVarint32LengthFieldPrepender protobufVarint32LengthFieldPrepender;
    public static final StringEncoder stringEncoder;

    static {
        protobufEncoder = SpringUtil.getBean("protobufEncoder");
        protobufVarint32LengthFieldPrepender = SpringUtil.getBean("protobufVarint32LengthFieldPrepender");
        stringEncoder = SpringUtil.getBean("stringEncoder");
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        Map<String, ChannelHandler> map = ctx.pipeline().toMap();

        if (msg instanceof BaseMsgProto.BaseMsg) {
            if (map.containsKey("stringEncoder")){
                ctx.pipeline().remove("stringEncoder");
            }

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
