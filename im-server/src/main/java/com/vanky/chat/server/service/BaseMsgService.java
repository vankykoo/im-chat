package com.vanky.chat.server.service;

import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.server.pojo.BaseMsg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86180
* @description 针对表【base_msg】的数据库操作Service
* @createDate 2024-03-30 20:44:18
*/
public interface BaseMsgService extends IService<BaseMsg> {

    /**
     * 保存消息
     * @param msg
     */
    void saveMsg(BaseMsgProto.BaseMsg msg);

    /**
     * 根据唯一id获取消息
     * @param uniqueId
     * @return
     */
    BaseMsg getBaseMsgByUniqueId(Long uniqueId);

    /**
     * 设为已读
     * @param uniqueId
     */
    void setMsgHasReadByUniqueId(Long uniqueId);

    /**
     * 设为已送达/未读
     * @param uniqueId
     */
    void setMsgHasNotReadByUniqueId(Long uniqueId);

    void setMsgHasNotReadByUniqueIds(List<Long> uniqueId);

    /**
     * 检查消息是否存在
     * @param uniqueId
     * @return
     */
    boolean isMsgExisted(Long uniqueId);

    /**
     * 已读消息
     * @param fromUserId
     * @param toUserId
     */
    List<Long> setMsgHasReadByUser(long fromUserId, long toUserId);

    /**
     * 用户拉取历史消息，每次100条
     * @param fromUserId
     * @param toUserId
     * @param oldestMsgId
     */
    List<BaseMsg> getHistoryMsg(Long fromUserId, Long toUserId, Long oldestMsgId);
}
