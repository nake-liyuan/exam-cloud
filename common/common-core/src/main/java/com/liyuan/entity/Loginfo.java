package com.liyuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liyuan
 * @date 2022/10/27
 * @project exam-cloud
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loginfo {

    private Long elapsedTime;
    private String description;
    private String region;
    private String username;
    private String date;
    private String token;
    private String clientIP;
    private String clientId;
    private String grantType;
    private Integer state;
}
