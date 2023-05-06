package com.liyuan.domain.app.params;

import lombok.Data;

/**
 * @author liyuan
 * @date 2023/1/12
 * @project exam-cloud
 */
@Data
public class UserAnswerInfo {
    private String id;
    private String userId;
    private String subjectId;
    private Integer childrenChapterCorrectNum;
    private Integer childrenChapterAnswerNum;
    private String userAnswer;
    private Integer isMistakes;
    private Integer answerNum;
    private Integer correctNum;
}
