package com.liyuan.service;

import com.liyuan.entity.applet.Message;

/**
 * @author liyuan
 * @date 2023/2/8
 * @project exam-cloud
 */
public interface MessagesService {


    void sendMessages(Message message);

    void delayedMessageSend(Message message);

}
