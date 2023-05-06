package com.liyuan.service.impl;


import cn.hutool.captcha.CaptchaUtil;
import com.liyuan.api.Result;
import com.liyuan.constant.AuthConstant;
import com.liyuan.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author liyuan
 * @date 2022/10/19
 * @project exam-cloud
 */
@SuppressWarnings({"unchecked","rawtypes"})
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final RedisTemplate redisTemplate;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public Result sendSimpleMail(String email) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, "UTF-8");
            //谁发的
            helper.setFrom(fromEmail);
            //谁要接收
            helper.setTo(email);
            //邮件标题
            helper.setSubject(AuthConstant.Email_verification_code_title);
            //邮件内容 这里引入的是Template的Context
            Context context = new Context();
            //设置模板中的变量
            String code = CaptchaUtil.createCircleCaptcha(100, 38, 4, 20).getCode();
            context.setVariable("verifyCode", code);
            // 第一个参数为模板的名称
            String process = templateEngine.process("email.html", context); //这里不用写全路径
            // 第二个参数true表示这是一个html文本
            helper.setText(process,true);
            helper.setSentDate(new Date());
            mailSender.send(mimeMessage);
            redisTemplate.opsForValue().set(AuthConstant.Email_verification_code+email, code, AuthConstant.Email_verification_code_Expiration_time, TimeUnit.SECONDS);
            return Result.success();
        } catch (MessagingException e) {
            e.printStackTrace();
            return Result.mailfailed();
        }


    }
}
