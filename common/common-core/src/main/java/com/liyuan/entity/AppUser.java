package com.liyuan.entity;

import lombok.Data;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
@Data
public class AppUser {
    private String nickName;
    private String gender;
    private String avatarUrl;
    private String openId;
}
