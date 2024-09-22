package com.vanky.chat.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vanky.chat.user.pojo.po.Relation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86180
* @description 针对表【relation】的数据库操作Mapper
* @createDate 2024-03-25 21:05:01
* @Entity generator.domain.Relation
*/
public interface RelationMapper extends BaseMapper<Relation> {
    /**
     * 根据用户id获取好友列表
     * @param userId
     * @return
     */
    List<Relation> selectFriendsByUserId(@Param("userId") Long userId);
}




