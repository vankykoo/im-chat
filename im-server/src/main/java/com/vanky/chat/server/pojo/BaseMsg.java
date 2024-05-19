package com.vanky.chat.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.protobuf.ByteString;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.common.utils.CommonConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @TableName base_msg
 */
@TableName(value ="base_msg")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseMsg implements Serializable {
    /**
     * 消息递增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息唯一id
     */
    @TableField(value = "unique_id")
    private Long uniqueId;

    /**
     * 发送方id
     */
    @TableField(value = "from_user_id")
    private Long fromUserId;

    /**
     * 接收方id
     */
    @TableField(value = "to_user_id")
    private Long toUserId;

    /**
     * 发送时间戳
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 聊天类型
     */
    @TableField(value = "chat_type")
    private Integer chatType;

    /**
     * 消息类型
     */
    @TableField(value = "msg_type")
    private Integer msgType;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private byte[] content;

    /**
     * 消息状态
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static BaseMsg Proto2BaseMsg(BaseMsgProto.BaseMsg msg){
        BaseMsg baseMsg = BaseMsg.builder()
                .id(msg.getId())
                .uniqueId(msg.getUniqueId())
                .fromUserId(msg.getFromUserId())
                .toUserId(msg.getToUserId())
                .createTime(new Date(msg.getCreateTime()))
                .chatType(msg.getChatType())
                .content(msg.getContent().toByteArray())
                .msgType(msg.getMsgType())
                .status(msg.getStatus())
                .build();

        return baseMsg;
    }

    public static BaseMsgProto.BaseMsg BaseMsg2Proto(BaseMsg baseMsg){
        ByteString byteStringContent = ByteString.copyFrom(baseMsg.getContent());

        BaseMsgProto.BaseMsg.Builder builder = BaseMsgProto.BaseMsg.newBuilder();
        builder.setId(baseMsg.getId())
                .setChatType(baseMsg.getChatType())
                .setContent(byteStringContent)
                .setUniqueId(baseMsg.getUniqueId())
                .setFromUserId(baseMsg.getFromUserId())
                .setToUserId(baseMsg.getToUserId())
                .setCreateTime(baseMsg.getCreateTime().getTime())
                .setMsgType(baseMsg.getMsgType())
                .setStatus(baseMsg.getStatus());

        return builder.build();
    }

    public static List<BaseMsgProto.BaseMsg> BaseMsg2Proto(List<BaseMsg> baseMsgList){
        return baseMsgList.stream().map(BaseMsg::BaseMsg2Proto).collect(Collectors.toList());
    }
}