package com.vanky.chat.client.processor;

import com.vanky.chat.client.netty.NettyClient;
import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.utils.SendAckMsgUtil;
import com.vanky.chat.client.utils.SendMsgUtil;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import com.vanky.chat.common.utils.CommonMsgGenerator;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author vanky
 * @create 2024/3/30 16:06
 */
@Component
@Slf4j
public class ForwardMsgProcessor {

    @Resource
    @Lazy
    private NettyClient nettyClient;

    @Resource
    private CommonMsgGenerator commonMsgGenerator;

    /**
     * 处理转发消息
     * @param msg
     */
    public void forwardMsg(BaseMsgProto.BaseMsg msg){
        //1.看看当前客户端channelMap有没有对应的服务端
        String address = CommonConverter.byteString2String(msg.getContent());
        String[] split = address.split(":");
        String host = split[0];
        int port = Integer.parseInt(split[1]);

        NioSocketChannel channel = UserChannelMap.hostChannel.get(address);

        BaseMsgProto.BaseMsg forwardMsg = commonMsgGenerator.generateForwardMsg(address);
        if (channel != null){
            //已经创建过了，给服务端发转发消息，让服务端去数据库查
            SendMsgUtil.sendMsg(channel, forwardMsg);
            //channel.writeAndFlush(forwardMsg);
        }

        //还没创建过，重新连接一个新的
        NioSocketChannel newChannel = nettyClient.connect(host, port, msg.getFromUserId());
        UserChannelMap.hostChannel.put(address, newChannel);
        SendMsgUtil.sendMsg(newChannel, forwardMsg);
        //newChannel.writeAndFlush(forwardMsg);

        log.info("客户端处理了来自服务端的转发消息！ {} ====> {}",
                msg.getFromUserId(), msg.getToUserId());
    }

}
