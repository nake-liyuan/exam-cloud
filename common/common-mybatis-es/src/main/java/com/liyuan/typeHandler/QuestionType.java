package com.liyuan.typeHandler;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author liyuan
 * @date 2022/10/25
 * @project exam-cloud
 */
public enum QuestionType {

    SINGLE(0, "单选题"),
    MULTIPLE(1, "多选题"),
    JUDGMENTAL(2, "判断题"),
    FILLEMPTY(3, "填空题"),
    SUBJECTIVE(4, "主观题");

    @EnumValue
    private int questiontype;
    @JsonValue
    private String value;

    QuestionType(int questiontype, String value) {
        this.questiontype = questiontype;
        this.value = value;
    }

    public int getQuestiontype() {
        return questiontype;
    }

    public String getValue() {
        return value;
    }
}
