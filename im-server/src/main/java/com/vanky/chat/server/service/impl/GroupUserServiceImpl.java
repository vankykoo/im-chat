package com.vanky.chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.common.bo.ImUserBo;
import com.vanky.chat.common.cache.OnlineCache;
import com.vanky.chat.common.cache.RedisCacheKey;
import com.vanky.chat.common.feign.userFeign.ImUserFeignClient;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.server.mapper.GroupMsgMapper;
import com.vanky.chat.server.pojo.GroupUser;
import com.vanky.chat.server.service.GroupService;
import com.vanky.chat.server.service.GroupUserService;
import com.vanky.chat.server.mapper.GroupUserMapper;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86180
* @description 针对表【group_user】的数据库操作Service实现
* @createDate 2024-03-30 22:02:47
*/
@Service
public class GroupUserServiceImpl extends ServiceImpl<GroupUserMapper, GroupUser>
    implements GroupUserService{

    @Resource
    private GroupUserMapper groupUserMapper;

    @Resource
    private ImUserFeignClient imUserFeignClient;

    @Resource
    @Lazy
    private GroupService groupService;

    @Resource
    private GroupMsgMapper groupMsgMapper;

    @Override
    public Long getLastAckIdByUserId(Long userId, Long groupId) {
        Long lastAckId = groupUserMapper.getLastAckIdByGroupIdAndUserId(groupId, userId);

        return lastAckId;
    }

    @Override
    public List<GroupUser> getByUserId(long userId) {
        LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(GroupUser::getUserId, userId);

        return this.list(wrapper);
    }

    @Override
    public void updateLastAckId(Long groupId, Long userId, Long lastAckId) {
        groupUserMapper.updateLastAckMsgId(groupId, userId, lastAckId);
    }

    @Override
    public Long setLastReadMsgId(long groupId, long userId){
        Long lastAckId = groupUserMapper.getLastAckIdByGroupIdAndUserId(groupId, userId);

        groupUserMapper.setLastReadMsgIdByGroupIdAndUserId(groupId, userId);

        return lastAckId;
    }

    @Override
    public List<GroupUser> getByGroupId(Long groupId) {
        return groupUserMapper.getByGroupId(groupId);
    }

    @Override
    public void joinGroup(Long userId, Long groupId) {
        //查用户名
        Result<ImUserBo> result = imUserFeignClient.getUserById(userId);
        if (result == null || !result.isSuccess()){
            throw new RuntimeException("远程调用失败！");
        }
        String username = result.getData().getUsername();

        //查最后一条消息的id
        Long uniqueId = groupMsgMapper.getLastMsgUniqueIdOfGroup(groupId);

        GroupUser groupUser = GroupUser.builder()
                .userGroupName(username)
                .lastAckMsgId(uniqueId)
                .lastReadMsgId(uniqueId)
                .groupId(groupId)
                .userId(userId)
                .build();

        this.save(groupUser);

        //更新群聊人数
        groupService.updateGroupUserNumber(groupId);

        //保存到群在线用户中
        RedisUtil.sput(OnlineCache.GROUP_ONLINE_USER + groupId, userId);
    }

    @Override
    public Boolean isUserInGroup(Long userId, Long groupId) {
        LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(GroupUser::getUserId, userId);
        wrapper.eq(GroupUser::getGroupId, groupId);

        return this.exists(wrapper);
    }
}




