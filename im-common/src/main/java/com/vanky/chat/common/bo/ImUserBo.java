package com.vanky.chat.common.bo;

import lombok.Data;

import java.util.*;

/**
 * @author vanky
 * @create 2024/5/10 19:40
 */
@Data
public class ImUserBo {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private Integer sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 上次离线时间
     */
    private Date offlineTime;

    /**
     * 用户的公钥
     */
    private String publicKey;

    /**
     * 帐号状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private Long updater;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    //用户 的 角色
    private Set<RoleBo> roles = new HashSet<>();

    //权限信息
    private List<String> permissions = new ArrayList<>();
}
