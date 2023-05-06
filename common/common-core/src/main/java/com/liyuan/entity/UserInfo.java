package com.liyuan.entity;

import lombok.Data;

/**
 * @author liyuan
 * @date 2022/10/25
 * @project exam-cloud
 */
@Data
public class UserInfo {
    private String phone;
    private String nickname;
    private String user_name;
    private String avatar;
    private String[] authorities;
}
