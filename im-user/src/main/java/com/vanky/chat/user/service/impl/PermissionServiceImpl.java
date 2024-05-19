package com.vanky.chat.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanky.chat.user.pojo.po.Permission;
import com.vanky.chat.user.service.PermissionService;
import com.vanky.chat.user.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author 86180
* @description 针对表【permission】的数据库操作Service实现
* @createDate 2024-05-10 11:03:40
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

}




