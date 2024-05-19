package com.vanky.chat.server.controller;

import com.vanky.chat.common.response.Result;
import com.vanky.chat.common.to.GroupTo;
import com.vanky.chat.common.to.GroupUserTo;
import com.vanky.chat.server.service.GroupService;
import com.vanky.chat.server.service.GroupUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author vanky
 * @create 2024/4/24 15:59
 */
@RestController
@RequestMapping("/server/group")
public class GroupController {

    @Resource
    private GroupService groupService;

    @Resource
    private GroupUserService groupUserService;

    @GetMapping("/getPublicKey")
    @Operation(summary = "获取群聊公钥")
    public Result<String> getPublicKey(Long groupId) {
        String publicKey = groupService.getPublicKey(groupId);

        return Result.success(publicKey);
    }

    @PostMapping("/createGroup")
    @Operation(summary = "创建群聊")
    public Result createGroup(@RequestBody GroupTo groupTo){
        groupService.createGroup(groupTo);

        return Result.success();
    }

    @PostMapping("/joinGroup")
    @Operation(summary = "加入群聊")
    public Result joinGroup(@RequestBody GroupUserTo groupUserTo){
        groupUserService.joinGroup(groupUserTo.getUserId(), groupUserTo.getGroupId());

        return Result.success();
    }

}
