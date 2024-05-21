package com.vanky.chat.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanky.chat.user.pojo.po.Relation;

/**
* @author 86180
* @description 针对表【relation】的数据库操作Service
* @createDate 2024-03-25 21:05:01
*/
public interface RelationService extends IService<Relation> {
    /**
     * 添加好友
     * @param fromUserId
     * @param toUserId
     */
    void addFriend(Long fromUserId, Long toUserId);

    /**
     * 判断用户是否为好友关系
     * @param fromUserId
     * @param toUserId
     * @return
     */
    Boolean areUsersFriends(Long fromUserId, Long toUserId);
}
