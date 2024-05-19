package com.vanky.chat.server.service;

import com.vanky.chat.common.to.GroupTo;
import com.vanky.chat.server.pojo.Group;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86180
* @description 针对表【group】的数据库操作Service
* @createDate 2024-03-30 22:02:47
*/
public interface GroupService extends IService<Group> {

    /**
     * 获取群聊的公钥
     * @param groupId
     * @return
     */
    String getPublicKey(Long groupId);

    /**
     * 创建群聊
     * @param groupTo
     */
    void createGroup(GroupTo groupTo);

    /**
     * 更新群聊人数
     * @param groupId
     */
    void updateGroupUserNumber(Long groupId);
}
