package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.entity.applet.Message;
import com.liyuan.service.MessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyuan
 * @date 2023/2/8
 * @project exam-cloud
 */
@RestController
@RequestMapping("messages")
@RequiredArgsConstructor
public class MessagesController {

    private final MessagesService messagesService;


    /**
     * @description: 延迟消息推送
     * @time: 2023/5/3 2:21
     * @param message 消息
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result<java.lang.String>
     */
    @PostMapping("/delayedSend")
    public Result<String> delayedMessagePush(@RequestBody Message message){
        messagesService.delayedMessageSend(message);
        return Result.success();
    }
}
