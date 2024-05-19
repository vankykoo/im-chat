package com.vanky.chat.server.service;

import com.vanky.chat.common.bo.OfflineGroupMsgDetailBo;
import com.vanky.chat.common.bo.OfflineMsgInfo;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.server.pojo.GroupMsg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86180
* @description 针对表【group_msg】的数据库操作Service
* @createDate 2024-03-30 22:02:47
*/
public interface GroupMsgService extends IService<GroupMsg> {

    /**
     * 保存群消息
     * @param msg
     */
    void saveMsg(BaseMsgProto.BaseMsg msg);

    /**
     * 获取用户未读群消息
     * @param userId
     * @param groupId
     * @return
     */
    List<GroupMsg> getUserHasNotAckGroupMsg(Long userId, Long groupId);

    /**
     * 消息是否已存在
     * @param uniqueId
     * @return
     */
    boolean isMsgExisted(String uniqueId);

    /**
     * 获取用户的离线时接收的消息的信息
     * @param userId
     * @return
     */
    List<OfflineMsgInfo> getOfflineMsgInfo(long userId);

    /**
     * 获取某个last_ack_id后面未被接收的100条消息
     * @param groupId
     * @param lastAckId
     */
    List<OfflineGroupMsgDetailBo> getFollowGroupMsg4LastAckIdLt100(Long groupId, Long lastAckId);

    /**
     * 根据uniqueId获取群聊消息
     * @param uniqueId
     * @return
     */
    GroupMsg getGroupMsgByUniqueId(long uniqueId);

    /**
     * 获取群聊最后一条消息uniqueId
     * @param groupId
     * @return
     */
    Long getLastMsgUniqueId(Long groupId);
}
