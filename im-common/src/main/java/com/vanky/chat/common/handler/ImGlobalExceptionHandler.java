package com.vanky.chat.common.handler;

import com.vanky.chat.common.exception.BaseException;
import com.vanky.chat.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author vanky
 * @create 2024/5/17 16:06
 */
@RestControllerAdvice
@Slf4j
public class ImGlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<Object> exceptionHandler(BaseException ex){
        log.error("异常信息 ：{}",ex.getMessage());
        return Result.error(ex.getMessage());
    }

}
