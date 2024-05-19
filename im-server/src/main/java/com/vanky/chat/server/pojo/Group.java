package com.vanky.chat.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName group
 */
@TableName(value ="group")
@Data
public class Group implements Serializable {
    /**
     * 群id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群名字
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * 创建人id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 群人数
     */
    @TableField(value = "user_number")
    private Integer userNumber;

    /**
     * 群号
     */
    @TableField(value = "unique_id")
    private Long uniqueId;

    /**
     * 群公钥
     */
    @TableField(value = "public_key")
    private String publicKey;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}