package com.liyuan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liyuan.domain.app.params.JoinArrival;
import com.liyuan.util.RabbitmqUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/rabbit")
@RequiredArgsConstructor
@RestController
@Slf4j
public class RabbitmqController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;


    @PostMapping("/sendMessage")
    public void sendDeLayMessage(@RequestBody JoinArrival joinArrival) throws JsonProcessingException {
        Map<String,Object> map = new HashMap<>();
        map.put("message",joinArrival.getMessage());
        map.put("userImage",joinArrival.getUserImage());
        map.put("date", joinArrival.getDate());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime activityTime = LocalDateTime.ofInstant(joinArrival.getDate().toInstant(), ZoneOffset.ofHours(8));
        int seconds = (int) Duration.between(now, activityTime).getSeconds()*1000;
        // 发送消息
        RabbitmqUtils.sendDelayMessage(rabbitTemplate,7000,objectMapper.writeValueAsString(map),System.currentTimeMillis());
        log.info("发送延迟队列消息成功：消息内容："+map);
    };
}
