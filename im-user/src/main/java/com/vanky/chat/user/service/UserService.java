package com.vanky.chat.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanky.chat.user.pojo.po.User;
import com.vanky.chat.user.pojo.to.UserTo;


/**
* @author vnaky
* @description 针对表【user】的数据库操作Service
* @createDate 2024-03-25 21:05:01
*/
public interface UserService extends IService<User> {

    /**
     * 获取用户的公钥
     * @param userId
     * @return
     */
    String getUserPublicKey(Long userId);
}
