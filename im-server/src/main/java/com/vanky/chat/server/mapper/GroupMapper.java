package com.vanky.chat.server.mapper;

import com.vanky.chat.server.pojo.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author 86180
* @description 针对表【group】的数据库操作Mapper
* @createDate 2024-03-30 22:02:47
* @Entity com.vanky.chat.server.pojo.Group
*/
public interface GroupMapper extends BaseMapper<Group> {

    /**
     * 获取群聊的公钥
     * @param groupId
     * @return
     */
    @Select("select public_key from `group` where id = #{groupId}")
    String getPublicKey(@Param("groupId") Long groupId);

    /**
     * 获取群聊人数
     * @param groupId
     * @return
     */
    @Select("select count(*) from group_user where group_id = #{groupId}")
    int getGroupUserNumber(@Param("groupId") Long groupId);

    /**
     * 修改群人数
     * @param groupId
     * @param userNumber
     */
    @Update("update `group` set user_number = #{userNumber} where id = #{groupId}")
    void updateUserNumber(@Param("groupId") Long groupId, @Param("userNumber") int userNumber);

    /**
     * 保存群聊
     * @param group
     */
    void insertGroup(@Param("group") Group group);
}




