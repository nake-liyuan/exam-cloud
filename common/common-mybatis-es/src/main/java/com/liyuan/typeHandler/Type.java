package com.liyuan.typeHandler;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author liyuan
 * @date 2022/10/25
 * @project exam-cloud
 */

public enum Type {
    CATEGORY(0, "类别"),
    NAME(1, "名称"),
    CHAPTER(2, "章节"),
    SECTION(3, "小节"),
    SUBJECT(4, "题目");

    @EnumValue
    private int type;
    @JsonValue
    private String value;


    Type(int type, String value) {
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
