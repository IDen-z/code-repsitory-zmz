package com.zmz.yygh.common.exception;

import com.zmz.yygh.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
    * @Description: 指定所有异常都进这个方法
    * @Author: Zhu Mengze
    * @Date: 2021/6/22 19:13
    */
    @ExceptionHandler(Exception.class)
    //方法中出现异常，就会进这个方法
    public Result<String> error(Exception exception){
        log.info("出现异常，异常具体信息为: {}",exception.getMessage());
        return Result.fail(exception.getMessage());
    }


    /**
    * @Description: 处理预约挂号异常
    * @Author: Zhu Mengze
    * @Date: 2021/6/22 19:46
    */
    @ExceptionHandler(YyghException.class)
    public Result error(YyghException exception){
        return Result.build(exception.getCode(), exception.getMessage());
    }
}







