package com.vanky.chat.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.common.bo.OfflineGroupMsgDetailBo;
import com.vanky.chat.common.bo.OfflineMsgInfo;
import com.vanky.chat.common.constant.TypeEnum;
import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.server.pojo.GroupMsg;
import com.vanky.chat.server.pojo.GroupUser;
import com.vanky.chat.server.service.GroupMsgService;
import com.vanky.chat.server.mapper.GroupMsgMapper;
import com.vanky.chat.server.service.GroupUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 86180
* @description 针对表【group_msg】的数据库操作Service实现
* @createDate 2024-03-30 22:02:47
*/
@Service
public class GroupMsgServiceImpl extends ServiceImpl<GroupMsgMapper, GroupMsg>
    implements GroupMsgService{

    @Resource
    private IdGeneratorFeignClient idGeneratorFeignClient;

    @Resource
    private GroupUserService groupUserService;

    @Resource
    private GroupMsgMapper groupMsgMapper;

    @Override
    public void saveMsg(BaseMsgProto.BaseMsg msg) {
        GroupMsg groupMsg = GroupMsg.proto2GroupMsg(msg);

        groupMsg.setId(idGeneratorFeignClient.nextId().getData());

        this.save(groupMsg);
    }

    @Override
    public List<GroupMsg> getUserHasNotAckGroupMsg(Long userId, Long groupId) {
        //获取用户的lastAckId
        Long lastAckId = groupUserService.getLastAckIdByUserId(userId, groupId);

        LambdaQueryWrapper<GroupMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMsg::getGroupId, groupId)
                .gt(GroupMsg::getUniqueId, lastAckId);

        List<GroupMsg> groupMsgs = this.list(wrapper);

        return groupMsgs;
    }

    @Override
    public boolean isMsgExisted(String uniqueId) {
        LambdaQueryWrapper<GroupMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupMsg::getUniqueId, uniqueId);

        return this.exists(wrapper);
    }

    @Override
    public List<OfflineMsgInfo> getOfflineMsgInfo(long userId) {
        //1.获取用户进入的群聊
        List<GroupUser> groupUserList = groupUserService.getByUserId(userId);

        //2.根据群用户信息获取未读消息
        List<OfflineMsgInfo> offlineMsgInfoList = new ArrayList<>();
        for (GroupUser groupUser : groupUserList) {
            OfflineMsgInfo offlineMsgInfo = getOfflineMsgInfo(groupUser);
            if (offlineMsgInfo == null){
                continue;
            }

            offlineMsgInfoList.add(offlineMsgInfo);
        }

        return offlineMsgInfoList;
    }

    @Override
    public List<OfflineGroupMsgDetailBo> getFollowGroupMsg4LastAckIdLt100(Long groupId, Long lastAckId) {
        List<OfflineGroupMsgDetailBo> list = groupMsgMapper.get100GroupMsgByGroupIdAndLastAckId(groupId, lastAckId);
        if (list == null || list.size() == 0){
            return null;
        }

        return list;
    }

    @Override
    public GroupMsg getGroupMsgByUniqueId(long uniqueId) {
        return groupMsgMapper.getByUniqueId(uniqueId);
    }

    @Override
    public Long getLastMsgUniqueId(Long groupId) {
        return groupMsgMapper.getLastMsgUniqueIdOfGroup(groupId);
    }

    /**
     * 根据群用户信息获取未读消息
     * @param groupUser
     * @return
     */
    private OfflineMsgInfo getOfflineMsgInfo(GroupUser groupUser){
        int count = groupMsgMapper.getOfflineMsgCount(groupUser);

        if (count == 0){
            return null;
        }

        GroupMsg firstMsg = groupMsgMapper.getFirstOfflineMsg(groupUser);
        GroupMsg lastMsg = groupMsgMapper.getLastOfflineMsg(groupUser);

        OfflineMsgInfo offlineMsgInfo = OfflineMsgInfo.builder()
                .fromUserId(groupUser.getGroupId())
                .gotoTimestamp(firstMsg.getUniqueId())
                .lastTimestamp(lastMsg.getUniqueId())
                .msgCount(count)
                .content(lastMsg.getContent().getBytes())
                .msgType(TypeEnum.MsgType.OFFLINE_GROUP_MSG_INFO.getValue())
                .build();

        return offlineMsgInfo;
    }


}




