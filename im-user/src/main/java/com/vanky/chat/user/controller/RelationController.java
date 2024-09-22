package com.vanky.chat.user.controller;

import com.vanky.chat.common.response.Result;
import com.vanky.chat.user.service.RelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author vanky
 * @create 2024/5/21 17:16
 */
@RestController
@RequestMapping("/user/relation")
@Tag(name = "好友关系维护相关接口")
public class RelationController {

    @Resource
    private RelationService relationService;

    @GetMapping(value = "/addFriend")
    @Operation(summary = "添加好友")
    public Result addFriend(@RequestParam("fromUserId") Long fromUserId,
                            @RequestParam("toUserId") Long toUserId){
        relationService.addFriend(fromUserId, toUserId);

        return Result.success();
    }

    @GetMapping("/areUsersFriends")
    @Operation(summary = "判断用户是否为好友关系")
    public Result<Boolean> areUsersFriends(@RequestParam("fromUserId")Long fromUserId,
                                    @RequestParam("toUserId")Long toUserId){
        Boolean areUsersFriends = relationService.areUsersFriends(fromUserId, toUserId);

        return Result.success(areUsersFriends);
    }

    @GetMapping(value = "/getFriendsByUserId")
    @Operation(summary = "根据用户id获取好友列表")
    public Result<List<Long>> getFriendsByUserId(@RequestParam("userId") Long userId){
        List<Long> friendsIdList = relationService.getFriendsByUserId(userId);

        return Result.success(friendsIdList);
    }
}
