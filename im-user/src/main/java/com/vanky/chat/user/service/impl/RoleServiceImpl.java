package com.vanky.chat.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.user.pojo.po.Role;
import com.vanky.chat.user.service.RoleService;
import com.vanky.chat.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 86180
* @description 针对表【role】的数据库操作Service实现
* @createDate 2024-05-10 11:03:40
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




