package com.liyuan.util;

import com.liyuan.config.QueueEnum;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author liyuan
 * @date 2023/3/13
 * @project exam-cloud
 */
public class RabbitmqUtils {
    /**
     * 发送延迟消息
     * @param rabbitTemplate rabbitTemplate
     * @param millisecond 延迟毫秒
     * @param messageContent 发送字符串
     * @param busiId 业务主键id
     */
    public static void sendDelayMessage(RabbitTemplate rabbitTemplate, Integer millisecond, Object messageContent, Long busiId) {
        CorrelationData correlationData = new CorrelationData(busiId.toString() + System.currentTimeMillis());
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_INFO_PLUGIN_SEND.getExchange(), QueueEnum.QUEUE_INFO_PLUGIN_SEND.getRouteKey(), messageContent,
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setDelay(millisecond);
                    return message;
                }, correlationData);
    }
}
