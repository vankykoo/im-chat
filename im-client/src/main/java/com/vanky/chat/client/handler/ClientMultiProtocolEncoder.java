package com.vanky.chat.client.handler;

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
public class ClientMultiProtocolEncoder extends MessageToMessageEncoder<Object> {

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

        log.info("*********************ClientMultiProtocolEncoder*******************************");

        if (msg instanceof BaseMsgProto.BaseMsg baseMsg) {
            log.info("识别为 BaseMsg 信息");

            if (map.containsKey("stringEncoder")){
                log.info("当前 pipeline 中有 String 编码器");

                ctx.pipeline().remove("stringEncoder");
            }

            if (!map.containsKey("protobufEncoder")){
                log.info("当前 pipeline 中没有 protobuf 编码器，添加中。。。");

                ctx.pipeline().addBefore("clientMultiProtocolEncoder", "protobufVarint32LengthFieldPrepender",
                        protobufVarint32LengthFieldPrepender);
                ctx.pipeline().addBefore("clientMultiProtocolEncoder", "protobufEncoder",
                        protobufEncoder);
            }

            out.add(baseMsg);
        } else if (msg instanceof String) {
            log.info("识别为 String 信息");

            if (map.containsKey("protobufEncoder")){
                log.info("当前 pipeline 中有 protobuf 编码器");

                ctx.pipeline().remove("protobufEncoder");
                ctx.pipeline().remove("protobufVarint32LengthFieldPrepender");
            }

            if (!map.containsKey("stringEncoder")){
                log.info("当前 pipeline 中没有 String 编码器，添加中。。。");

                ctx.pipeline().addBefore("clientMultiProtocolEncoder", "stringEncoder",
                        stringEncoder);
            }

            out.add(msg);
        }

        log.info("****************************************************");
    }
}
