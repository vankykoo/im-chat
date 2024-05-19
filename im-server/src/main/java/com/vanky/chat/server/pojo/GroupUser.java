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
 * @TableName group_user
 */
@TableName(value ="group_user")
@Data
@Builder
public class GroupUser implements Serializable {

    /**
     * id
     */
    @TableField(value = "id")
    private Long id;

    /**
     * 群成员id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 上次ack的消息id
     */
    @TableField(value = "last_ack_msg_id")
    private Long lastAckMsgId;

    /**
     * 进群时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 群昵称
     */
    @TableField(value = "user_group_name")
    private String userGroupName;

    /**
     * 群id
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 已读消息最大id
     */
    @TableField(value = "last_read_msg_id")
    private Long lastReadMsgId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}