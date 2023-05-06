package com.liyuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.domain.AnswerRecord;
import com.liyuan.domain.app.params.UserAnswerInfo;

/**
 * @author liyuan
 * @date 2022/11/27
 * @project exam-cloud
 */
public interface AnswerRecordService extends IService<AnswerRecord> {

    boolean userAnswerInfoSubmit(UserAnswerInfo userAnswerInfo);

    boolean userRecordSubject(AnswerRecord answerRecord);

    void clearUserChapterAnswerRecords(String id,String subjectId);

}
