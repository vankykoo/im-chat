package com.vanky.chat.common.to;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author vanky
 * @create 2024/3/31 10:40
 */
@Data
public class GroupMsgTo {

    /**
     * 群id
     */
    private Long groupId;

    /**
     * 发送方id
     */
    private Long userId;

    /**
     * 消息内容
     */
    private String content;

}
