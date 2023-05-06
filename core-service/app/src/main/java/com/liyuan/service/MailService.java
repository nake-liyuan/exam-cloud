package com.liyuan.service;

import com.liyuan.api.Result;

import javax.mail.MessagingException;

/**
 * @author liyuan
 * @date 2022/10/19
 * @project exam-cloud
 */
public interface MailService {

    Result sendSimpleMail(String email) throws MessagingException;

}
