package com.ape.apeadmin.controller;

import com.ape.apecommon.domain.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author shaozhujie
 * @version 1.0
 * @description: 全局异常处理类
 * @date 2023/9/14 10:55
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return Result.fail(e.getMessage());
    }

}
