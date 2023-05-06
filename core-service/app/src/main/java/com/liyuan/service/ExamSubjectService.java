package com.liyuan.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.api.Result;
import com.liyuan.domain.ExamSubject;
import com.liyuan.domain.app.vo.UserSubjectInfo;
import com.liyuan.entity.dto.AnswersInfoByDay;

import java.util.List;
import java.util.Map;

/**
 * @author liyuan
 * @date 2022/11/24
 * @project exam-cloud
 */
public interface ExamSubjectService extends IService<ExamSubject> {

    List<Tree<String>> getExamSubjectOption();

    List<Tree<String>> getChapterInfo(String id);

    List<UserSubjectInfo> getsubjectInfoBySection(String id);

    List<UserSubjectInfo> getWrongQuestionSet(String id);

    List<UserSubjectInfo> getFavorites(String id);
    List<UserSubjectInfo> getNotepad(String id);

    Map<String,Object> randomQuery(String id );

    void submitAnswersByDay(AnswersInfoByDay answersInfoByDay);

    String getUserCurrentSubject(String key);

    void setUserSubjectQuestionInfo(String id,String questionId,Integer option,Integer state);

    Boolean getUserAnswerInfo(String id,Integer option);

    String getChapterId(String id);
}
