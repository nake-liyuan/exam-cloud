package com.liyuan.typeHandler;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author liyuan
 * @date 2022/11/7
 * @project exam-cloud
 */
public enum UserStatus {
    DISABLE(0, "禁用"),
    ENABLE(1, "启用");

    @EnumValue
    private int status;
    @JsonValue
    private String value;

    UserStatus(int status, String value) {
        this.status = status;
        this.value = value;
    }

    public int getType() {
        return status;
    }

    public String getValue() {
        return value;
    }
}
