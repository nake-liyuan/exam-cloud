package com.liyuan.typeHandler;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author liyuan
 * @date 2022/11/13
 * @project exam-cloud
 */
public enum Hidden {
    DISPLAY(0, false),
    Hidden(1, true);

    @EnumValue
    private int hidden;
    @JsonValue
    private Boolean value;

    Hidden(int hidden, Boolean value) {
        this.hidden = hidden;
        this.value = value;
    }

    public int getHidden() {
        return hidden;
    }

    public Boolean getValue() {
        return value;
    }
}
