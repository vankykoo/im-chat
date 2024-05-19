package com.vanky.chat.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName offline_msg
 */
@TableName(value ="offline_msg")
@Data
@Builder
public class OfflineMsg implements Serializable {
    /**
     * 消息id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息唯一id
     */
    @TableField(value = "unique_id")
    private Long uniqueId;

    /**
     * 接收方id
     */
    @TableField(value = "to_user_id")
    private Long toUserId;

    /**
     * 发送时间
     */
    @TableField(value = "create_time")
    private Long createTime;

    /**
     * 发送方id
     */
    @TableField(value = "from_user_id")
    private Long fromUserId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}