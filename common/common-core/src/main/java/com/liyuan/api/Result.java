package com.liyuan.api;

import com.liyuan.exceptionhandler.YKException;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @description: 统一响应结构体
 * @author: LiYuan
 * @time: 2023/4/5 3:45
 */
@Data
public class Result<T> implements Serializable {

    private String code;

    private T data;

    private String msg;


    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> failed() {
        return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), ResultCode.SYSTEM_EXECUTION_ERROR.getMsg(), null);
    }

    public static <T> Result<T> failed(String msg) {
        return result(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(), msg, null);
    }

    public static <T> Result<T> failed(String code,String msg) {
        return result(code, msg, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized(T data) {
        return  result(ResultCode.TOKEN_INVALID_OR_EXPIRED.getCode(),ResultCode.TOKEN_INVALID_OR_EXPIRED.getMsg(),data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden(T data) {
        return  result(ResultCode.ACCESS_UNAUTHORIZED.getCode(), ResultCode.ACCESS_UNAUTHORIZED.getMsg(), data);
    }

    /**
     * 邮件提醒服务失败
     */
    public static <T> Result<T> mailfailed() {
        return result(ResultCode.Mail_SERVICE_ERROR.getCode(), ResultCode.Mail_SERVICE_ERROR.getMsg(), null);
    }

    /**
     * 邮件提醒服务失败
     */
    public static <T> Result<T> mailcheckfailed() {
        return result(ResultCode.Mail_CheckCode_ERROR.getCode(), ResultCode.Mail_CheckCode_ERROR.getMsg(), null);
    }

    public static <T> Result<T> YKException(YKException ykException) {
        return result(ykException.getCode(), ykException.getMsg(), null);
    }

    public static <T> Result<T> judge(boolean status) {
        if (status) {
            return success();
        } else {
            return failed();
        }
    }

    public static <T> Result<T> checkcode(boolean status) {
        if (status) {
            return success();
        } else {
            return failed("验证码有误");
        }
    }

    public static <T> Result<T> checkuser(boolean status,String username,String phone,String email) {
        if (status) {
            return success();
        } else {
            if(StringUtils.hasText(username)){
                return failed(ResultCode.USER_EXISTENCE.getCode(),ResultCode.USER_EXISTENCE.getMsg());
            }
            if (StringUtils.hasText(phone)){
                return failed(ResultCode.PHONE_EXISTENCE.getCode(),ResultCode.PHONE_EXISTENCE.getMsg());
            }
            if(StringUtils.hasText(email)){
                return failed(ResultCode.EMAIL_EXISTENCE.getCode(),ResultCode.EMAIL_EXISTENCE.getMsg());
            }
            return failed(ResultCode.SYSTEM_EXECUTION_ERROR.getCode(),ResultCode.SYSTEM_EXECUTION_ERROR.getMsg());
        }

    }

    public static <T> Result<T> failed(IResultCode resultCode) {
        return result(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public static <T> Result<T> failed(IResultCode resultCode, String msg) {
        return result(resultCode.getCode(), msg, null);
    }

    private static <T> Result<T> result(IResultCode resultCode, T data) {
        return result(resultCode.getCode(), resultCode.getMsg(), data);
    }

    private static <T> Result<T> result(String code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static boolean isSuccess(Result<?> result) {
        return result != null && ResultCode.SUCCESS.getCode().equals(result.getCode());
    }


}
