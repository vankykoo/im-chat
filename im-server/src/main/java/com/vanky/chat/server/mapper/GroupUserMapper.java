package com.vanky.chat.server.mapper;

import com.vanky.chat.server.pojo.GroupUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 86180
* @description 针对表【group_user】的数据库操作Mapper
* @createDate 2024-03-30 22:02:47
* @Entity com.vanky.chat.server.pojo.GroupUser
*/
public interface GroupUserMapper extends BaseMapper<GroupUser> {

    /**
     * 更新群用户的lastAckMsgId
     * @param groupId
     * @param userId
     * @param lastAckId
     */
    @Update("update group_user set last_ack_msg_id = #{lastAckId} " +
            "where group_id = #{groupId} and user_id = #{userId}")
    void updateLastAckMsgId(@Param("groupId") Long groupId, @Param("userId") Long userId, @Param("lastAckId") Long lastAckId);

    /**
     * 修改已读消息id
     * @param groupId
     * @param userId
     */
    @Update("update group_user set last_read_msg_id = last_ack_msg_id " +
            "where group_id = #{groupId} and user_id = #{userId}")
    void setLastReadMsgIdByGroupIdAndUserId(@Param("groupId") long groupId, @Param("userId") long userId);

    /**
     * 获取用户的last_ack_msg_id
     * @param groupId
     * @param userId
     * @return
     */
    @Select("select last_ack_msg_id from group_user where group_id = #{groupId} and user_id = #{userId}")
    Long getLastAckIdByGroupIdAndUserId(@Param("groupId") long groupId, @Param("userId") long userId);

    /**
     * 查询一个群内的所有用户
     * @param groupId
     * @return
     */
    List<GroupUser> getByGroupId(@Param("groupId") Long groupId);
}




