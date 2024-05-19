package com.vanky.chat.common.to;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vanky
 * @create 2024/4/25 17:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupTo {

    /**
     * 群名字
     */
    private String groupName;

    /**
     * 创建人id
     */
    private Long userId;

}
