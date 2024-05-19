package com.vanky.chat.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群消息表
 * @TableName group_msg
 */
@TableName(value ="group_msg")
@Data
@Builder
public class GroupMsg implements Serializable {
    /**
     * 群消息id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群消息唯一id
     */
    @TableField(value = "unique_id")
    private Long uniqueId;

    /**
     * 发送方id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 发送时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 群id
     */
    @TableField(value = "group_id")
    private Long groupId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static GroupMsg proto2GroupMsg(BaseMsgProto.BaseMsg msg){
        GroupMsg groupMsg = GroupMsg.builder()
                .id(msg.getId())
                .uniqueId(msg.getUniqueId())
                .groupId(msg.getToUserId())
                .createTime(new Date(msg.getCreateTime()))
                .content(CommonConverter.byteString2String(msg.getContent()))
                .userId(msg.getFromUserId())
                .build();

        return groupMsg;
    }

    public static GroupMsg baseMsg2GroupMsg(BaseMsg baseMsg){
        GroupMsg groupMsg = GroupMsg.builder()
                .id(baseMsg.getId())
                .uniqueId(baseMsg.getUniqueId())
                .groupId(baseMsg.getToUserId())
                .createTime(baseMsg.getCreateTime())
                .content(baseMsg.getContent().toString())
                .userId(baseMsg.getFromUserId())
                .build();

        return groupMsg;
    }

    public static BaseMsgProto.BaseMsg groupMsg2Proto(GroupMsg groupMsg){
        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();
        builder.setId(groupMsg.getId())
                .setChatType(TypeEnum.ChatType.GROUP_CHAT.getValue())
                .setContent(CommonConverter.string2ByteString(groupMsg.getContent()))
                .setUniqueId(groupMsg.getUniqueId())
                .setFromUserId(groupMsg.getUserId())
                .setToUserId(groupMsg.getGroupId())
                .setCreateTime(groupMsg.getCreateTime().getTime())
                .setMsgType(TypeEnum.MsgType.CHAT_MSG.getValue())
                .setStatus(TypeEnum.MsgStatus.HAS_NOT_READ.getValue());

        return builder.build();
    }

    public static List<BaseMsgProto.BaseMsg> groupMsg2Proto(List<GroupMsg> groupMsgs){
        return groupMsgs.stream().map(GroupMsg::groupMsg2Proto).collect(Collectors.toList());
    }

    public static BaseMsg groupMsg2BaseMsg(GroupMsg groupMsg, byte[] bytesContent) {
        BaseMsg baseMsg = BaseMsg.builder()
                .id(groupMsg.getId())
                .uniqueId(groupMsg.getUniqueId())
                .fromUserId(groupMsg.getUserId())
                .toUserId(groupMsg.getGroupId())
                .createTime(groupMsg.getCreateTime())
                .chatType(TypeEnum.ChatType.GROUP_CHAT.getValue())
                .content(bytesContent)
                .msgType(TypeEnum.MsgType.CHAT_MSG.getValue())
                .status(TypeEnum.MsgStatus.SENT.getValue())
                .build();

        return baseMsg;
    }
}