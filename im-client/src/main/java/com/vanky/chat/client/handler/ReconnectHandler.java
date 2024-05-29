package com.vanky.chat.client.handler;

import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.netty.NettyClient;
import com.vanky.chat.client.utils.MsgGenerator;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author vanky
 * @create 2024/5/27 16:03
 */
@Component
@Slf4j
public class ReconnectHandler {

    private int retryTimes = 10;

    @Resource
    private NettyClient nettyClient;

    @Resource
    private MsgGenerator msgGenerator;

    //1. 不断尝试重连，每隔一段时间
    //2. 重连成功时就返回
    public NioSocketChannel reconnect(Long userId, int type){
        if (userId == null){
            throw new NullPointerException("用户id为空？");
        }

        NioSocketChannel channel = null;

        for(int i = 1; i <= retryTimes; i++){
            try {
                log.info("正在尝试第 {} 次重连...", i);

                channel = nettyClient.connect("localhost", 20003);

                if (channel != null){
                    break;
                }

                Thread.sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (channel == null){
            log.warn("重连失败，请检查网络！");
        }else {
            if (type == 0){
                //私信连接
                UserChannelMap.userChannel.put(userId, channel);
            }else{
                //群聊连接
                UserChannelMap.groupChannel.put(userId, channel);
            }
            UserChannelMap.channelUserMap.put(channel.id().asLongText(), userId);

            //登录前检查本地有没有私钥，如果没有就要生成，而且把公钥传给客户端

            //这里放一个登录的消息
            BaseMsgProto.BaseMsg msg = msgGenerator.generateLoginMsg(userId);
            channel.writeAndFlush(msg);

            log.info("重连成功！");
        }

        return channel;
    }

}
