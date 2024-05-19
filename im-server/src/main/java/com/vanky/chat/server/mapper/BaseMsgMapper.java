package com.vanky.chat.server.mapper;

import com.vanky.chat.server.pojo.BaseMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 86180
* @description 针对表【base_msg】的数据库操作Mapper
* @createDate 2024-03-30 20:44:18
* @Entity com.vanky.chat.server.pojo.BaseMsg
*/
public interface BaseMsgMapper extends BaseMapper<BaseMsg> {

    /**
     * 根据uniqueId获取消息
     * @param uniqueIds
     * @return
     */
    List<BaseMsg> selectByUniqueIds(@Param("uniqueIds") List<Long> uniqueIds);

    /**
     * 消息设为未读/已送达
     * @param uniqueId
     */
    @Update("update base_msg set status = 0 where unique_id = #{uniqueId}")
    void setHasNotReadByUniqueId(@Param("uniqueId") Long uniqueId);

    void setHasNotReadByUniqueIds(@Param("uniqueIds") List<Long> uniqueIds);

    @Select("select * from base_msg where unique_id = #{uniqueId};")
    BaseMsg selectByUniqueId(@Param("uniqueId") Long uniqueId);

    /**
     * 获取未读消息
     * @param fromUserId
     * @param toUserId
     * @return
     */
    List<Long> getBaseMsgByFromUserIdAndToUserId(@Param("fromUserId") long fromUserId, @Param("toUserId") long toUserId);

    /**
     * 将消息设置为已读
     * @param uniqueIds
     * @return
     */
    void setHasReadByUniqueId(@Param("uniqueIds") List<Long> uniqueIds);
}




