package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.service.SmsService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
@RestController
@RequestMapping("sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService service;

    /**
     * @description 发送短信验证码
     * @param phone
     */
    @ApiOperation(value = "发送短信验证码")
    @PostMapping
    public Result sendSmsCode(@RequestParam("phone") String phone)  {
        boolean result = service.sendSms(phone);
        return Result.judge(result);
    }
}
