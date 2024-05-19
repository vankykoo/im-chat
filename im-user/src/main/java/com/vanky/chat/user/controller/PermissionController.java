package com.vanky.chat.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vanky.chat.common.bo.PermissionBo;
import com.vanky.chat.common.cache.RedisSimpleCacheName;
import com.vanky.chat.common.exception.MyException;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.utils.RedisUtil;
import com.vanky.chat.user.mapper.PermissionMapper;
import com.vanky.chat.user.pojo.po.Permission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vanky
 * @create 2024/5/10 21:47
 */
@RestController
@RequestMapping("/user/permission")
@Tag(name = "权限相关接口")
@Slf4j
public class PermissionController {

    @Resource
    private PermissionMapper permissionMapper;

    @GetMapping("/path")
    @Operation(summary = "获取所访问的路径所需的权限")
    public Result<PermissionBo> getPathRequestPermission(String uri){
        //到redis中获取
        String cacheKey = RedisSimpleCacheName.PATH_REQUEST_PERMISSION + uri;
        Permission permission = RedisUtil.get(cacheKey, Permission.class);

        if (permission == null){
            log.warn("redis 中没有路径【{}】对应的权限", uri);
            permission = permissionMapper
                    .selectOne(new LambdaQueryWrapper<Permission>().eq(Permission::getPath, uri));
        }

        if (permission == null){
            throw new MyException.DataNotExistedException("访问路径无效！");
        }

        PermissionBo permissionBo = new PermissionBo();
        BeanUtils.copyProperties(permission, permissionBo);

        return Result.success(permissionBo);
    }

}
