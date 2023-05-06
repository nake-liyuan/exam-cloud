package com.liyuan.domain.app.vo;

import com.liyuan.typeHandler.QuestionType;
import lombok.Data;

/**
 * @author liyuan
 * @date 2022/11/27
 * @project exam-cloud
 */
@Data
public class UserSubjectInfo {
    private String id;
    private String name;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String userAnswer;
    private Integer answersNum;
    private Integer answerNum;
    private Integer correctNum;
    private String referenceAnswer;
    private String analysis;
    private QuestionType questionType;
    private String topicNote;
    private Integer isMistakes;
    private Integer isCollect;

}
