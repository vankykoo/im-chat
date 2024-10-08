package com.vanky.chat.client.controller;

import com.google.protobuf.ByteString;
import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.pojo.to.PullHistoryMsgTo;
import com.vanky.chat.client.utils.MsgGenerator;
import com.vanky.chat.common.ApplicationContext;
import com.vanky.chat.common.bo.PrivateMsgBo;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.cache.RedisSimpleCacheName;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.to.PrivateMsgTo;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import com.vanky.chat.common.utils.RedisUtil;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.spdy.SpdyHttpResponseStreamIdHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private MsgGenerator msgGenerator;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    @PostMapping("/send")
    @Operation(summary = "私发消息")
    public Result send(@RequestBody PrivateMsgTo privateMsgTo, @RequestHeader("X-User-id") String context){

        Long headerUserId = Long.parseLong(context);
        if (Long.compareUnsigned(privateMsgTo.getFromUserId(), headerUserId) != 0){
            throw new MyException.ImAuthenticationException("认证失败，别用别人的token访问接口！");
        }

        ApplicationContext.setUserId(privateMsgTo.getFromUserId());

        // 修改消息内容
        privateMsgTo.setContent(privateMsgTo.getContent() + "---" + System.currentTimeMillis());

        //消息加密
        ByteString byteStringContent = msgEncryptUtil
                .msgEncrypt(privateMsgTo.getContent(),
                        privateMsgTo.getToUserId(),
                        privateMsgTo.getFromUserId(),
                        TypeEnum.ChatType.PRIVATE_CHAT.getValue());

        //生成私信消息
        BaseMsgProto.BaseMsg msg = msgGenerator
                .generatePrivateMsg(PrivateMsgBo.to2bo(privateMsgTo, byteStringContent));

        NioSocketChannel channel = UserChannelMap.getChannel(privateMsgTo.getFromUserId());
        channel.writeAndFlush(msg);

        return Result.success();
    }

    @GetMapping("/hasRead")
    @Operation(summary = "已读私发消息")
    public Result hasRead(@RequestParam("fromUserId") Long fromUserId,
                       @RequestParam("toUserId") Long toUserId,
                          @RequestHeader("X-User-id") String context){
        Long headerUserId = Long.parseLong(context);
        if (Long.compareUnsigned(toUserId, headerUserId) != 0){
            throw new MyException.ImAuthenticationException("认证失败，别用别人的token访问接口！");
        }

        BaseMsgProto.BaseMsg msg = msgGenerator
                .generateHasReadNoticeMsg(fromUserId, toUserId, TypeEnum.ChatType.PRIVATE_CHAT);

        NioSocketChannel channel = UserChannelMap.getChannel(fromUserId);
        channel.writeAndFlush(msg);

        return Result.success();
    }

    @GetMapping("/history")
    @Operation(summary = "获取历史消息")
    public Result pullHistoryMsg(@RequestBody PullHistoryMsgTo pullHistoryMsgTo,
                                               @RequestHeader("X-User-id") String context){
        Long headerUserId = Long.parseLong(context);
        if (Long.compareUnsigned(pullHistoryMsgTo.getToUserId(), headerUserId) != 0){
            throw new MyException.ImAuthenticationException("认证失败，别用别人的token访问接口！");
        }

        // 修改该用户在redis（本地）中的oldestMsgId
        String cacheKey = RedisCacheKey.PRIVATE_OLDEST_MSG_ID_KEY +
                pullHistoryMsgTo.getFromUserId() +
                RedisSimpleCacheName.UNION_KEY +
                pullHistoryMsgTo.getToUserId();

        Long oldestMsgId = RedisUtil.get(cacheKey, Long.class);

        if (oldestMsgId == null){
            oldestMsgId = Long.MAX_VALUE;
        }

        BaseMsgProto.BaseMsg msg = msgGenerator.
                generatePullHistoryMsg(pullHistoryMsgTo.getFromUserId(),
                        pullHistoryMsgTo.getToUserId(), oldestMsgId);

        NioSocketChannel channel = UserChannelMap.getChannel(pullHistoryMsgTo.getToUserId());
        channel.writeAndFlush(msg);

        return Result.success();
    }

    @PostMapping("/send30")
    @Operation(summary = "私发消息30条消息")
    public Result send100(@RequestBody PrivateMsgTo privateMsgTo,
                          @RequestHeader("X-User-id") String context){
        Long headerUserId = Long.parseLong(context);
        if (Long.compareUnsigned(privateMsgTo.getFromUserId(), headerUserId) != 0){
            throw new MyException.ImAuthenticationException("认证失败，别用别人的token访问接口！");
        }

        ApplicationContext.setUserId(privateMsgTo.getFromUserId());

        NioSocketChannel channel = UserChannelMap.getChannel(privateMsgTo.getFromUserId());

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
