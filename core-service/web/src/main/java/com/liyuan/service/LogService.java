package com.liyuan.service;

import com.liyuan.api.Result;

import java.util.Date;

/**
 * @author liyuan
 * @date 2023/3/11
 * @project exam-cloud
 */
public interface LogService {

    Result logByPage(Integer pageNum, Integer pageSize,
                     String serviceName, String type, Date date);

    Result loginLogByPage(Integer pageNum, Integer pageSize, Date date,
                          String username, String clientIP,Date startTime,Date endTime);

    void forcedOffline(String token);

}
