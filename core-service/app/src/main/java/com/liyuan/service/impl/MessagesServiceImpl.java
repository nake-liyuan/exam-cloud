package com.liyuan.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liyuan.api.AppletClient;
import com.liyuan.domain.User;
import com.liyuan.entity.applet.Message;
import com.liyuan.mapper.mp.UserMapper;
import com.liyuan.service.MessagesService;
import com.liyuan.util.RabbitmqUtils;
import com.liyuan.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * @author liyuan
 * @date 2023/2/8
 * @project exam-cloud
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessagesServiceImpl implements MessagesService {

    @Value("${wechat.weapp.appid}")
    private String appid;

    @Value("${wechat.weapp.secret}")
    private String secret;

    private final UserMapper userMapper;
    private final AppletClient appletClient;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendMessages(Message message) {
        String accessToken = JSONUtil.parseObj(appletClient.getPerson("client_credential", appid, secret)).getStr("access_token");
        appletClient.sendTemplateMessage(accessToken,message);
    }

    @SneakyThrows
    @Override
    public void delayedMessageSend(Message message) {
        //获取用户的openId
        String openId = userMapper.selectOne(new QueryWrapper<User>().select("open_id").eq("id", UserUtils.getUserId())).getOpenId();
        message.setTouser(openId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime activityTime = JSONUtil.parseObj(JSONUtil.parseObj(JSONUtil.toJsonStr(message.getData())).getStr("time17")).getLocalDateTime("value", now);
        int millisecond = (int) ((Duration.between(now, activityTime).getSeconds()*1000)-180000);
        //推送延迟消息
        RabbitmqUtils.sendDelayMessage(rabbitTemplate,millisecond,objectMapper.writeValueAsString(message),System.currentTimeMillis());
    }
}
