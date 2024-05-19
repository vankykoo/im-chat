package com.vanky.chat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.protobuf.BaseMsgProto;
import com.vanky.chat.server.pojo.BaseMsg;
import com.vanky.chat.server.pojo.OfflineMsg;
import com.vanky.chat.common.bo.OfflineMsgInfo;

import java.util.List;

/**
* @author 86180
* @description 针对表【offline_msg】的数据库操作Service
* @createDate 2024-03-30 16:42:36
*/
public interface OfflineMsgService extends IService<OfflineMsg> {

    /**
     * 保存离线消息
     * @param msg
     */
    void saveOfflineMsg(BaseMsgProto.BaseMsg msg);

    void saveOfflineMsg(BaseMsg baseMsg);

    /**
     * 根据用户id获取离线消息
     * @param userId
     * @return
     */
    List<OfflineMsgInfo> getOfflineMsgInfoByUserId(Long userId);

    /**
     * 根据uniqueId删除离线消息
     * @param uniqueIds
     */
    void deleteOfflineMsgByUniqueId(List<Long> uniqueIds);

    /**
     * 根据离线消息基本信息获取100条离线消息
     * @param fromUserId 发送者id
     * @param toUserId 接收消息用户id
     * @return
     */
    List<OfflineMsgDetailBo> get100OfflineMsg(Long fromUserId,  Long toUserId);
}
