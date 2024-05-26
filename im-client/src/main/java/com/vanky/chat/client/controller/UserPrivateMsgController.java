package com.vanky.chat.client.controller;

import com.google.protobuf.ByteString;
import com.vanky.chat.client.netty.NettyClient;
import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.utils.MsgGenerator;
import com.vanky.chat.common.ApplicationContext;
import com.vanky.chat.common.bo.PrivateMsgBo;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.to.PrivateMsgTo;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author vanky
 * @create 2024/3/30 11:06
 */
@RestController
@RequestMapping("/client/private")
@Tag(name = "私信")
@Slf4j
public class UserPrivateMsgController {

    @Resource
    private NettyClient nettyClient;

    @Resource
    private MsgGenerator msgGenerator;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    @PostMapping("/send")
    @Operation(summary = "私发消息")
    public Result send(@RequestBody PrivateMsgTo privateMsgTo){
        log.info("发送私信消息开始时间：{} ",System.currentTimeMillis());

        ApplicationContext.setUserId(privateMsgTo.getFromUserId());

        //消息加密
        ByteString byteStringContent = msgEncryptUtil
                .msgEncrypt(privateMsgTo.getContent(),
                        privateMsgTo.getToUserId(),
                        privateMsgTo.getFromUserId(),
                        TypeEnum.ChatType.PRIVATE_CHAT.getValue());

        //生成私信消息
        BaseMsgProto.BaseMsg msg = msgGenerator
                .generatePrivateMsg(PrivateMsgBo.to2bo(privateMsgTo, byteStringContent));

        NioSocketChannel channel = (NioSocketChannel) UserChannelMap
                .userChannel.get(privateMsgTo.getFromUserId());

        if (channel == null || !channel.isActive()){
            //重新建立连接
            log.info("客户端无该用户【{}】连接，正在重连~",privateMsgTo.getFromUserId());
            channel = nettyClient.connect("localhost", 20003);
            UserChannelMap.userChannel.put(privateMsgTo.getFromUserId(), channel);
        }

        channel.writeAndFlush(msg);

        return Result.success();
    }

    @GetMapping("/hasRead")
    @Operation(summary = "已读私发消息")
    public Result hasRead(@RequestParam("fromUserId") Long fromUserId,
                       @RequestParam("toUserId") Long toUserId){

        BaseMsgProto.BaseMsg msg = msgGenerator
                .generateHasReadNoticeMsg(fromUserId, toUserId, TypeEnum.ChatType.PRIVATE_CHAT);

        NioSocketChannel channel = (NioSocketChannel) UserChannelMap.userChannel.get(toUserId);

        if (channel == null || !channel.isActive()){
            //重新建立连接
            channel = nettyClient.connect("localhost", 20003);
            UserChannelMap.userChannel.put(toUserId, channel);
        }

        channel.writeAndFlush(msg);

        return Result.success();
    }

    @PostMapping("/send30")
    @Operation(summary = "私发消息30条消息")
    public Result send100(@RequestBody PrivateMsgTo privateMsgTo){

        ApplicationContext.setUserId(privateMsgTo.getFromUserId());

        NioSocketChannel channel = (NioSocketChannel) UserChannelMap
                .userChannel.get(privateMsgTo.getFromUserId());

        if (channel == null || !channel.isActive()){
            //重新建立连接
            channel = nettyClient.connect("localhost", 20003);
            UserChannelMap.userChannel.put(privateMsgTo.getFromUserId(), channel);
        }

        String content = privateMsgTo.getContent();
        for(int i = 0; i < 30; i++){
            privateMsgTo.setContent(content + i);

            //消息加密
            ByteString byteStringContent = msgEncryptUtil
                    .msgEncrypt(privateMsgTo.getContent(),
                            privateMsgTo.getToUserId(),
                            privateMsgTo.getFromUserId(),
                            TypeEnum.ChatType.PRIVATE_CHAT.getValue());

            BaseMsgProto.BaseMsg msg = msgGenerator
                    .generatePrivateMsg(PrivateMsgBo.to2bo(privateMsgTo, byteStringContent));

            channel.writeAndFlush(msg);
        }

        return Result.success("发送成功！");
    }

}
