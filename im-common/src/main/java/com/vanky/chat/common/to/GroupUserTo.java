package com.vanky.chat.common.to;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author vanky
 * @create 2024/4/25 17:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserTo {

    /**
     * 群成员id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 群id
     */
    private Long groupId;

}
