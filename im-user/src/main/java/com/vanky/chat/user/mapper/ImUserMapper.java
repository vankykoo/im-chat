package com.vanky.chat.user.mapper;

import com.vanky.chat.user.pojo.po.ImUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vanky.chat.user.pojo.po.Relation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86180
* @description 针对表【im_user】的数据库操作Mapper
* @createDate 2024-05-10 11:20:17
* @Entity com.vanky.chat.user.pojo.po.ImUser
*/
public interface ImUserMapper extends BaseMapper<ImUser> {

    /**
     * 联表查询 获取用户的基本信息和角色
     * @param username
     * @return
     */
    ImUser selectByUsername(@Param("username") String username);

    /**
     * 根据用户的id查询用户的公钥
     * @param userId
     * @return
     */
    @Select("select public_key from im_user where user_id = #{userId}")
    String getUserPublicKey(@Param("userId") Long userId);

    /**
     * 根据用户id获取用户信息
     * @param userId
     * @return
     */
    ImUser selectByUserId(Long userId);
}




