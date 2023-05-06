package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.service.MailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

/**
 * @author liyuan
 * @date 2022/10/19
 * @project exam-cloud
 */
@RestController
@RequestMapping("mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    /**
     * @description 发送邮件验证码
     * @param email
     */
    @ApiOperation(value = "发送邮件验证码")
    @PostMapping
    public Result sendMail(
            @ApiParam("账号") @RequestParam String email) throws MessagingException {
        mailService.sendSimpleMail(email);
        return Result.success();
    }
}
