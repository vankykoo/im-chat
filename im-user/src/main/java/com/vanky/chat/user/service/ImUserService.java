package com.vanky.chat.user.service;

import com.vanky.chat.user.pojo.po.ImUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vanky.chat.user.pojo.to.UserTo;

import java.util.List;

/**
* @author 86180
* @description 针对表【im_user】的数据库操作Service
* @createDate 2024-05-10 11:20:17
*/
public interface ImUserService extends IService<ImUser> {

    /**
     * 根据用户名查询用户信息
     * 并通过联表查询查到用户的角色和权限
     * @param username
     * @return
     */
    ImUser getUserByUserName(String username);

    /**
     * 用户注册
     * @param userTo
     */
    boolean register(UserTo userTo);

    /**
     * 获取用户的公钥
     * @param userId
     * @return
     */
    String getUserPublicKey(Long userId);

    /**
     * 根据用户id查询用户
     * @param userId
     * @return
     */
    ImUser getByUserId(Long userId);
}
