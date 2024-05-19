package com.vanky.chat.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author vanky
 * @create 2024/4/21 20:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CanalModel {

    /**
     * 数据库名
     */
    private String dbName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 操作类型
     */
    private String eventType;

    private String dataId;

    private String before;

    private String after;

}
