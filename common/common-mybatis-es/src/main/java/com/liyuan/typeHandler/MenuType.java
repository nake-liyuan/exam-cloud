package com.liyuan.typeHandler;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author liyuan
 * @date 2022/10/27
 * @project exam-cloud
 */
public enum MenuType {
    COMPONENT(0, "目录"),
    IFRAME(1, "菜单");

    @EnumValue
    private int type;
    @JsonValue
    private String value;


    MenuType(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
