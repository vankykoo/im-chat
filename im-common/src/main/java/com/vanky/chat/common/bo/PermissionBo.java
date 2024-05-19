package com.vanky.chat.common.bo;

import lombok.Data;

import java.util.Date;

/**
 * @author vanky
 * @create 2024/5/10 21:44
 */
@Data
public class PermissionBo {

    /**
     * 主键
     */
    private Long permissionId;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 菜单名
     */
    private String permissionName;

    /**
     * 匹配路径
     */
    private String path;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 类型：0，目录，1菜单，2：按钮
     */
    private Integer permissionType;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
