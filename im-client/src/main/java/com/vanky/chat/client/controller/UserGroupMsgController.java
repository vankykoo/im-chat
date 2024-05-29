package com.vanky.chat.client.controller;

import com.google.protobuf.ByteString;
import com.vanky.chat.client.channel.UserChannelMap;
import com.vanky.chat.client.utils.MsgGenerator;
import com.vanky.chat.common.ApplicationContext;
import com.vanky.chat.common.bo.GroupMsgBo;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.to.GroupMsgTo;
import com.vanky.chat.common.utils.MsgEncryptUtil;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author vanky
 * @create 2024/3/30 22:06
 */
@RestController
@RequestMapping("/client/group")
@Tag(name = "群消息相关接口")
@Slf4j
public class UserGroupMsgController {
    @Resource
    private MsgGenerator msgGenerator;

    @Resource
    private MsgEncryptUtil msgEncryptUtil;

    @PostMapping("/send")
    @Operation(summary = "发送群聊消息")
    public Result send(@RequestBody GroupMsgTo groupMsgTo, @RequestHeader("X-User-id") String context){
        Long headerUserId = Long.parseLong(context);
        if (Long.compareUnsigned(groupMsgTo.getUserId(), headerUserId) != 0){
            throw new MyException.ImAuthenticationException("认证失败，别用别人的token访问接口！");
        }

        log.info("发送群聊消息开始时间：{} ",System.currentTimeMillis());
        ApplicationContext.setUserId(groupMsgTo.getUserId());

        NioSocketChannel channel = UserChannelMap.getGroupChannel(groupMsgTo.getUserId());

        //消息加密
        ByteString byteStringContent = msgEncryptUtil
                .msgEncrypt(groupMsgTo.getContent(),
                        groupMsgTo.getGroupId(),
                        groupMsgTo.getUserId(),
                        TypeEnum.ChatType.GROUP_CHAT.getValue());

        BaseMsgProto.BaseMsg msg = msgGenerator
                .generateGroupMsg(GroupMsgBo.to2bo(groupMsgTo, byteStringContent));

        channel.writeAndFlush(msg);

        return Result.success();
    }

    //用于测试
    @PostMapping("/send30")
    @Operation(summary = "发送30条群聊消息")
    public Result send100(@RequestBody GroupMsgTo groupMsgTo, @RequestHeader("X-User-id") String context){
        Long headerUserId = Long.parseLong(context);
        if (Long.compareUnsigned(groupMsgTo.getUserId(), headerUserId) != 0){
            throw new MyException.ImAuthenticationException("认证失败，别用别人的token访问接口！");
        }
        NioSocketChannel channel = UserChannelMap.getGroupChannel(groupMsgTo.getUserId());

        String content = groupMsgTo.getContent();
        for (int i = 0; i < 30; i++) {
            groupMsgTo.setContent(content + i);

            ByteString byteStringContent = msgEncryptUtil
                    .msgEncrypt(groupMsgTo.getContent(),
                            groupMsgTo.getGroupId(),
                            groupMsgTo.getUserId(),
                            TypeEnum.ChatType.GROUP_CHAT.getValue());

            BaseMsgProto.BaseMsg msg = msgGenerator
                    .generateGroupMsg(GroupMsgBo.to2bo(groupMsgTo, byteStringContent));

            channel.writeAndFlush(msg);
        }

        return Result.success("发送成功");
    }

    @GetMapping("/hasRead")
    @Operation(summary = "已读群聊消息")
    public Result send(@RequestParam("groupId") Long groupId,
                       @RequestParam("userId") Long userId,
                       @RequestHeader("X-User-id") String context){
        Long headerUserId = Long.parseLong(context);
        if (Long.compareUnsigned(userId, headerUserId) != 0){
            throw new MyException.ImAuthenticationException("认证失败，别用别人的token访问接口！");
        }

        BaseMsgProto.BaseMsg msg = msgGenerator.generateHasReadNoticeMsg(groupId, userId, TypeEnum.ChatType.GROUP_CHAT);

        NioSocketChannel channel = UserChannelMap.getGroupChannel(userId);

        channel.writeAndFlush(msg);

        return Result.success();
    }


}
