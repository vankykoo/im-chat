package com.vanky.chat.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.user.pojo.po.UserRole;
import com.vanky.chat.user.service.UserRoleService;
import com.vanky.chat.user.mapper.UserRoleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author 86180
* @description 针对表【user_role】的数据库操作Service实现
* @createDate 2024-05-10 11:03:40
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public void updateUserRole(Long userId, long roleKey) {
        // 获取用户权限
        UserRole userRole = userRoleMapper.selectByUserId(userId);

        if (userRole == null){
            // 如果不存在就新增
            userRole = new UserRole(userId, roleKey);
            userRoleMapper.insert(userRole);
        } else if (userRole.getRoleId() != roleKey){
            // 存在就修改
            userRole.setRoleId(roleKey);
            userRoleMapper.updateById(userRole);
        }
    }

}




