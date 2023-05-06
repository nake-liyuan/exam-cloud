package com.liyuan.typeHandler;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author liyuan
 * @date 2022/11/7
 * @project exam-cloud
 */
public enum Sex {

    MALE(0, "女"),
    FEMALE(1, "男");

    @EnumValue
    private int type;
    @JsonValue
    private String value;


    Sex(int type, String value) {
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
