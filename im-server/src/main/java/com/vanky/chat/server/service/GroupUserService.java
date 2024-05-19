package com.vanky.chat.server.service;

import com.vanky.chat.common.to.GroupUserTo;
import com.vanky.chat.server.pojo.Group;
import com.vanky.chat.server.pojo.GroupUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86180
* @description 针对表【group_user】的数据库操作Service
* @createDate 2024-03-30 22:02:47
*/
public interface GroupUserService extends IService<GroupUser> {

    /**
     * 获取用户的lastAckId
     * @param userId
     * @return
     */
    Long getLastAckIdByUserId(Long userId, Long groupId);

    /**
     * 获取用户加入的所有群
     * @param userId
     * @return
     */
    List<GroupUser> getByUserId(long userId);

    /**
     * 更新LastAckId
     * @param groupId
     * @param userId
     * @param lastAckId
     */
    void updateLastAckId(Long groupId, Long userId, Long lastAckId);

    /**
     * 修改已读消息的最后一条消息id
     * @param groupId
     * @param userId
     */
    Long setLastReadMsgId(long groupId, long userId);

    /**
     * 查询群内的所有用户
     * @param groupId
     * @return
     */
    List<GroupUser> getByGroupId(Long groupId);

    /**
     * 用户加入群聊
     * @param userId
     * @param groupId
     */
    void joinGroup(Long userId, Long groupId);
}
