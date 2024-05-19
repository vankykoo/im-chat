package com.vanky.chat.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.user.pojo.po.UserRole;
import com.vanky.chat.user.service.UserRoleService;
import com.vanky.chat.user.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 86180
* @description 针对表【user_role】的数据库操作Service实现
* @createDate 2024-05-10 11:03:40
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




