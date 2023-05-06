package com.liyuan.enums;

import com.liyuan.util.IBaseEnum;
import lombok.Getter;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
public enum AuthenticationIdentityEnum implements IBaseEnum<String> {

    USERNAME("username", "用户名"),
    MOBILE("mobile", "手机号"),
    OPENID("openId", "开放式认证系统唯一身份标识");

    @Getter
    private String value;

    @Getter
    private String label;

    AuthenticationIdentityEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
