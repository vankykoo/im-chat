package com.vanky.chat.leaf.controller;

import com.vanky.chat.common.feign.leafFeign.IdGeneratorFeignClient;
import com.vanky.chat.common.response.Result;
import com.vanky.chat.leaf.utils.SnowflakeIdWorker;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vanky
 * @create 2024/4/13 20:44
 */
@Slf4j
@RestController
@RequestMapping("/id")
public class IdGeneratorController{

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @GetMapping(value = "/nextId")
    @Operation(summary = "生成id")
    public Result<Long> nextId(){
        long id = snowflakeIdWorker.nextId();

        log.info("生成全局唯一且递增id ：{}", id);

        return Result.success(id);
    }

}
