package com.vanky.chat.user.controller;

import com.vanky.chat.common.bo.ImUserBo;
import com.vanky.chat.common.bo.UserBo;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.user.mapper.ImUserMapper;
import com.vanky.chat.user.pojo.po.ImUser;
import com.vanky.chat.user.pojo.po.User;
import com.vanky.chat.user.pojo.to.UserTo;
import com.vanky.chat.user.service.ImUserService;
import com.vanky.chat.user.service.RelationService;
import com.vanky.chat.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author vanky
 * @create 2024/3/25 21:13
 */
@RequestMapping("/user")
@Tag(name = "用户相关接口")
@RestController
@Slf4j
public class UserController {

    @Resource
    private ImUserService imUserService;


    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result register(@RequestBody UserTo userTo){
        imUserService.register(userTo);

        return Result.success();
    }

    @GetMapping(value = "/getUserPublicKey")
    @Operation(summary = "获取用户的公钥")
    public Result<String> getUserPublicKey(@RequestParam("userId") Long userId) {
        String publicKey = imUserService.getUserPublicKey(userId);

        return Result.success(publicKey);
    }

    @GetMapping(value = "/getUserById")
    @Operation(summary = "根据id获取用户")
    public Result<ImUserBo> getUserById(@RequestParam("userId") Long userId){
        ImUser user = imUserService.getByUserId(userId);
        ImUserBo imUserBo = new ImUserBo();
        BeanUtils.copyProperties(user,imUserBo);

        return Result.success(imUserBo);
    }

    @GetMapping(value = "/getUserByUserName")
    @Operation(summary = "根据username获取用户")
    public Result<ImUserBo> getUserByUserName(@RequestParam("username") String username){
        ImUser imUser = imUserService.getUserByUserName(username);
        if (imUser == null){
            return Result.success();
        }

        ImUserBo imUserBo = new ImUserBo();
        BeanUtils.copyProperties(imUser, imUserBo);

        return Result.success(imUserBo);
    }
}
