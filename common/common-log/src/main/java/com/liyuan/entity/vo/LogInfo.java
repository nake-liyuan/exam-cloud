package com.liyuan.entity.vo;

import lombok.Data;

/**
 * @author liyuan
 * @date 2023/3/11
 * @project exam-cloud
 */
@Data
public class LogInfo {

    //服务名称
    private String service;
    //描述
    private String ip;
    //
    private String method;
    //耗时
    private String spendTime;
    //线程
    private String thread;
    //uri
    private String uri;
    //描述
    private String description;
    //发起时间
    private String timestamp;
    //信息
    private String message;

}
