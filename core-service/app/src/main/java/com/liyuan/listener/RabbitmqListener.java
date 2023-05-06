package com.liyuan.listener;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liyuan.api.AppletClient;
import com.liyuan.entity.applet.Message;
import com.liyuan.service.MessagesService;
import com.liyuan.service.impl.MessagesServiceImpl;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liyuan
 * @date 2023/3/13
 * @project exam-cloud
 */
@Slf4j
@Component
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@RabbitListener(queues = "LAZY_QUEUE")
public class RabbitmqListener {
    private final ObjectMapper objectMapper;
    private final MessagesService messagesService;

    @RabbitHandler
    public void onCustomBookingMessage(@Payload String message, Channel channel, @Headers Map<String, Object> headers) {
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {
            Message templateMessage = objectMapper.readValue(message, Message.class);
            messagesService.sendMessages(templateMessage);
            ack(channel,deliveryTag,"");
        } catch (JsonProcessingException e) {
            nack(channel,deliveryTag,"");
        }
    }

    /**
     * 确认消息成功
     */
    public void ack(Channel channel, long deliveryTag, String info) {
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            log.error(info + ":消息应答出错", e);
        }
    }

    /**
     * 确认消息失败
     */
    public void nack(Channel channel, long deliveryTag, String info) {
        try {
            channel.basicNack(deliveryTag, false, true);
        } catch (IOException e) {
            log.error(info + ":消息拒绝出错", e);
        }
    }


}
