package com.vanky.chat.user.pojo.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.*;

import lombok.Data;

/**
 * 
 * @TableName im_user
 */
@TableName(value ="im_user")
@Data
public class ImUser implements Serializable{
    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;

    /**
     * 用户账号
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机号码
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 头像地址
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 上次离线时间
     */
    @TableField(value = "offline_time")
    private Date offlineTime;

    /**
     * 用户的公钥
     */
    @TableField(value = "public_key")
    private String publicKey;

    /**
     * 帐号状态（0正常 1停用）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建者
     */
    @TableField(value = "creator")
    private Long creator;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(value = "updater")
    private Long updater;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 逻辑删除
     */
    @TableField(value = "deleted")
    private Integer deleted;

    //用户 的 角色
    @TableField(exist = false)
    private Set<Role> roles = new HashSet<>();

    //权限信息
    @TableField(exist = false)
    private List<String> permissions = new ArrayList<>();

    public ImUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}