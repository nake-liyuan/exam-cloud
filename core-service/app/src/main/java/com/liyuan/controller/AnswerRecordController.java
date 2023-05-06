package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.domain.AnswerRecord;
import com.liyuan.domain.app.params.UserAnswerInfo;
import com.liyuan.service.AnswerRecordService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyuan
 * @date 2022/11/27
 * @project exam-cloud
 */
@RestController
@RequestMapping("answerRecord")
@RequiredArgsConstructor
public class AnswerRecordController {

    private final AnswerRecordService service;

    /**
     * @description 答题信息状态提交
     * @date
     */
    @ApiOperation(value = "答题信息状态提交")
    @PutMapping("/submitAnswer")
    public Result userAnswerInfoSubmit(@RequestBody UserAnswerInfo userAnswerInfo)  {
        boolean edit = service.userAnswerInfoSubmit(userAnswerInfo);
        return Result.judge(edit);
    }

    /**
     * @description 用户记录题目
     * @date
     */
    @ApiOperation(value = "用户记录题目")
    @PutMapping("/recordSubject")
    public Result userRecordSubject(@RequestBody AnswerRecord answerRecord){
        boolean edit = service.userRecordSubject(answerRecord);
        return Result.judge(edit);
    }


    /**
     * @description:
     * @time: 2023/5/1 20:35
     * @param id 章节ID
     * @param subjectId 学科ID
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @ApiOperation(value = "清空用户章节答题记录（按学科）")
    @DeleteMapping("/chapter_records")
    public Result clearUserChapterAnswerRecords(@RequestParam String id,
                                                @RequestParam String subjectId){
        service.clearUserChapterAnswerRecords(id,subjectId);
        return Result.success();
    }

}
