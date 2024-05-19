package com.vanky.chat.server.controller;

import com.vanky.chat.common.response.Result;
import com.vanky.chat.server.netty.NettyServer;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vanky
 * @create 2024/3/26 16:25
 */
@RestController
public class Controller {

    @Resource
    private NettyServer nettyServer;

    @GetMapping("/startServer")
    public Result startServer(){
        nettyServer.run();

        return Result.success();
    }

}
