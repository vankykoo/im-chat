package com.vanky.chat.common.bo;

import lombok.Data;

import java.util.Date;

/**
 * RBAC 角色
 * @author vanky
 * @create 2024/5/10 19:34
 */
@Data
public class RoleBo {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色标识
     */
    private String roleLabel;

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0：可用，1：不可用
     */
    private Integer status;

    /**
     * 是否删除：0: 未删除，1：已删除
     */
    private Integer deleted;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
