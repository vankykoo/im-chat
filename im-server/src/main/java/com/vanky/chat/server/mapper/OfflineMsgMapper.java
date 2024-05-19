package com.vanky.chat.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vanky.chat.common.bo.OfflineMsgDetailBo;
import com.vanky.chat.common.bo.OfflineMsgInfo;
import com.vanky.chat.server.pojo.OfflineMsg;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86180
* @description 针对表【offline_msg】的数据库操作Mapper
* @createDate 2024-03-30 16:42:36
* @Entity generator.domain.OfflineMsg
*/
public interface OfflineMsgMapper extends BaseMapper<OfflineMsg> {


    /**
     * 根据uniqueId删除离线消息
     * @param uniqueIds
     */
    void deleteByUniqueId(@Param("uniqueIds") List<Long> uniqueIds);

    /**
     * 查询谁发送了新信息
     * @param userId
     * @return
     */
    List<Long> getUserIdWhoSendNewMsg(@Param("userId") Long userId);

    /**
     * 获取一个用户发了几条新消息
     * @param fromUserId
     * @param toUserId
     * @return
     */
    int getOfflineMsgCountOfOne(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    /**
     * 获取用户最后一条新消息
     * @param fromUserId
     * @param toUserId
     * @return
     */
    OfflineMsg getLastOfflineMsgOfOne(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    /**
     * 获取用户第一条新消息
     * @param fromUserId
     * @param toUserId
     * @return
     */
    OfflineMsg getFirstOfflineMsgOfOne(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    /**
     * 根据离线消息的基本信息获取全量离线消息
     * @param fromUserId
     * @param toUserId
     * @return
     */
    List<OfflineMsgDetailBo> selectAllOfflineMsgByInfo(@Param("fromUserId") Long fromUserId,
                                                       @Param("toUserId") Long toUserId);
}




