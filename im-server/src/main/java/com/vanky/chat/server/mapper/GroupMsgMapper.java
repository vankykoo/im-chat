package com.vanky.chat.server.mapper;

import com.vanky.chat.common.bo.OfflineGroupMsgDetailBo;
import com.vanky.chat.server.pojo.GroupMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vanky.chat.server.pojo.GroupUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86180
* @description 针对表【group_msg】的数据库操作Mapper
* @createDate 2024-03-30 22:02:47
* @Entity com.vanky.chat.server.pojo.GroupMsg
*/
public interface GroupMsgMapper extends BaseMapper<GroupMsg> {

    /**
     * 获取该群第一条未读消息
     * @param groupUser
     * @return
     */
    GroupMsg getFirstOfflineMsg(@Param("groupUser") GroupUser groupUser);

    /**
     * 获取该群最后一条未读消息
     * @param groupUser
     * @return
     */
    GroupMsg getLastOfflineMsg(@Param("groupUser") GroupUser groupUser);

    /**
     * 获取该群未读消息数量
     * @param groupUser
     * @return
     */
    int getOfflineMsgCount(@Param("groupUser") GroupUser groupUser);

    /**
     * 获取某个群聊中uniqueId大于lastAckId的消息
     * @param groupId
     * @param lastAckId
     */
    List<OfflineGroupMsgDetailBo> get100GroupMsgByGroupIdAndLastAckId(@Param("groupId") Long groupId, @Param("lastAckId") Long lastAckId);

    /**
     * 根据uniqueId获取群聊消息
     * @param uniqueId
     * @return
     */
    @Select("select * from group_msg where unique_id = #{uniqueId}")
    GroupMsg getByUniqueId(@Param("uniqueId") long uniqueId);

    /**
     * 获取群聊最后一条消息的uniqueId
     * @param groupId
     * @return
     */
    Long getLastMsgUniqueIdOfGroup(@Param("groupId") Long groupId);
}




