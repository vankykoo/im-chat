package com.vanky.chat.user.mapper;

import com.vanky.chat.user.pojo.po.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
* @author 86180
* @description 针对表【permission】的数据库操作Mapper
* @createDate 2024-05-10 11:03:40
* @Entity com.vanky.chat.user.pojo.po.Permission
*/
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> selectPermissionByRoleIds(@Param("roleIds") Set<Long> roleIds);
}




