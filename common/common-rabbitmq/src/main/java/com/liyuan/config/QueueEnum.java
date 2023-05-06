package com.liyuan.config;

import lombok.Getter;

/**
 * @author liyuan
 * @date 2022/6/28
 * @name online-problem
 */
@Getter
public enum QueueEnum {
    /**
     * 插件实现延迟队列
     */
    QUEUE_INFO_PLUGIN_SEND("SEND_SUBSCRIBE_INFO", "LAZY_QUEUE", "WECHAT_INFO");

    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}