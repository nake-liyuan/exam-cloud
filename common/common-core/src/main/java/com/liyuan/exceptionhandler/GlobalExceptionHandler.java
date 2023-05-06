package com.liyuan.exceptionhandler;

import com.liyuan.api.Result;
import com.liyuan.api.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author liyuan
 * @date 2022/5/30
 * @name cloud_question_bank
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.failed();
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.failed(ResultCode.Mail_CheckCode_ERROR.getMsg());
    }

    @ExceptionHandler(YKException.class)
    @ResponseBody
    public Result error(YKException e){
        e.printStackTrace();
        return Result.failed(e.getCode(),e.getMsg());
    }


}
