package com.liyuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.constant.AuthConstant;
import com.liyuan.domain.AnswerRecord;
import com.liyuan.domain.ExamSubject;
import com.liyuan.domain.app.params.UserAnswerInfo;
import com.liyuan.mapper.mp.AnswerRecordMapper;
import com.liyuan.mapper.mp.ExamSubjectMapper;
import com.liyuan.service.AnswerRecordService;
import com.liyuan.service.RedisService;
import com.liyuan.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyuan
 * @date 2022/11/27
 * @project exam-cloud
 */
@Service
@RequiredArgsConstructor
public class AnswerRecordServiceImpl extends ServiceImpl<AnswerRecordMapper, AnswerRecord> implements AnswerRecordService {

    private final ExamSubjectMapper examSubjectMapper;
    private final RedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean userAnswerInfoSubmit(UserAnswerInfo userAnswerInfo) {
        boolean userAnswerInfoSubmit=false;
        //用户子章节答题信息更新
        userAnswerInfoSubmit= this.update(
                new UpdateWrapper<AnswerRecord>()
                        .eq("user_id", userAnswerInfo.getUserId())
                        .eq("subject_id", userAnswerInfo.getSubjectId())
                        .set("answer_num", userAnswerInfo.getChildrenChapterAnswerNum())
                        .set("correct_num", userAnswerInfo.getChildrenChapterCorrectNum())

        );
        if(userAnswerInfoSubmit){
            //用户答题信息更新
            userAnswerInfoSubmit = this.update(
                    new UpdateWrapper<AnswerRecord>()
                            .eq("id", userAnswerInfo.getId())
                            .set("user_answer", userAnswerInfo.getUserAnswer())
                            .set("is_mistakes", userAnswerInfo.getIsMistakes())
                            .set("answer_num", userAnswerInfo.getAnswerNum())
                            .set("correct_num", userAnswerInfo.getCorrectNum())

            );
        }
        return userAnswerInfoSubmit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean userRecordSubject(AnswerRecord answerRecord) {

        boolean userRecordSubject = this.update(
                new UpdateWrapper<AnswerRecord>()
                        .eq("id", answerRecord.getId())
                        //收藏
                        .set(answerRecord.getIsCollect() != null, "is_collect", answerRecord.getIsCollect())
                        //修改笔记
                        .set(StringUtils.hasText(answerRecord.getTopicNote()), "topic_note", answerRecord.getTopicNote())
        );
        return userRecordSubject;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void clearUserChapterAnswerRecords(String id,String subjectId) {
        boolean update = this.update(new UpdateWrapper<AnswerRecord>()
                .eq("user_id", UserUtils.getUserId())
                .eq("subject_id", id)
                .set("answer_num", 0)
                .set("correct_num", 0)
        );
        if(update){
            List<ExamSubject> examSubjects = examSubjectMapper.selectList(new QueryWrapper<ExamSubject>()
                    .eq("pid", id).select("id"));
            List<String> problemId = examSubjects.stream().map(ExamSubject::getId).collect(Collectors.toList());
            this.update(new UpdateWrapper<AnswerRecord>()
                    .in("subject_id",problemId)
                    .eq("user_id", UserUtils.getUserId())
                    .set("answer_num", 0)
                    .set("correct_num", 0)
                    .set("is_mistakes",0)
                    .set("user_answer","")
            );
            redisService.del(AuthConstant.USER_SUBJECT_WRONG_ID+UserUtils.getUserId()+":"+subjectId);
        }
    }

}
