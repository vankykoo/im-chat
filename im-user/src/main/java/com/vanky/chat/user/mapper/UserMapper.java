package com.vanky.chat.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vanky.chat.user.pojo.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author 86180
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-03-25 21:05:01
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {

    @Select("select public_key from user where id = #{userId}")
    String getUserPublicKey(@Param("userId") Long userId);
}




