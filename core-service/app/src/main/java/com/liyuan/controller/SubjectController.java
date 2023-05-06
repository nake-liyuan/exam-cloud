package com.liyuan.controller;

import cn.hutool.core.lang.tree.Tree;
import com.liyuan.api.Result;
import com.liyuan.constant.AuthConstant;
import com.liyuan.domain.app.vo.UserSubjectInfo;
import com.liyuan.entity.dto.AnswersInfoByDay;
import com.liyuan.service.ExamSubjectService;
import com.liyuan.util.UserUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author liyuan
 * @date 2022/10/30
 * @project exam-cloud
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("subject")
@RequiredArgsConstructor
public class SubjectController {

    private final ExamSubjectService service;

    /**
     * @description 学科选项查询
     */
    @ApiOperation("学科选项查询")
    @GetMapping("/option")
    public Result Option(){
        List<Tree<String>> examSubjectOption = service.getExamSubjectOption();
        return Result.success(examSubjectOption);
    }

    /**
     * @description 获取用户当前备考学科
     */
    @ApiOperation("获取用户当前备考学科")
    @GetMapping
    public Result getUserCurrentSubject(){
        String userCurrentSubject = service.getUserCurrentSubject(AuthConstant.USER_CURRENT_SUBJECT_CODE_PREFIX +UserUtils.getUserId());
        return Result.success(userCurrentSubject);
    }

    /**
     * @description 学科章节信息
     * @param id 学科id
     */
    @ApiOperation("学科章节信息")
    @GetMapping("/chapterInfo")
    public Result chapterInfo(@RequestParam("id") String id){
        List<Tree<String>> examSubjectOption = service.getChapterInfo(id);
        return Result.success(examSubjectOption);
    }
    /**
     * @description 题目信息
     * @param id 学科ID
     */
    @ApiOperation("题目信息")
    @GetMapping("/subjectInfo")
    public Result subjectInfo(@RequestParam("id") String id){
        List<UserSubjectInfo> userSubjectInfos = service.getsubjectInfoBySection(id);
        return Result.success(userSubjectInfos);
    }

    /**
     * @description: 得到错误的问题集
     * @time: 2023/4/30 0:46
     * @param id 学科ID
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @ApiOperation("得到错误的问题集")
    @GetMapping("/wrong")
    public Result getWrong(@RequestParam("id") String id){
        List<UserSubjectInfo> userSubjectInfos = service.getWrongQuestionSet(id);
        return Result.success(userSubjectInfos);
    }

    /**
     * @description: 收藏夹
     * @time: 2023/4/30 0:48
     * @param id 学科ID
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @ApiOperation("收藏夹")
    @GetMapping("/favorites")
    public Result getFavorites(@RequestParam("id") String id){
        List<UserSubjectInfo> userSubjectInfos = service.getFavorites(id);
        return Result.success(userSubjectInfos);
    }

    /**
     * @description: 记事本
     * @time: 2023/4/30 0:49
     * @param id  学科ID
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @ApiOperation("记事本")
    @GetMapping("/notepad")
    public Result getNotepad(@RequestParam("id") String id){
        List<UserSubjectInfo> userSubjectInfos = service.getNotepad(id);
        return Result.success(userSubjectInfos);
    }

    /**
     * @description: 设置用户学科问题信息：（错题、收藏、笔记）
     * @time: 2023/4/30 2:21
     * @param id 学科ID
     * @param questionId 题目ID
     * @param option 选项（0->错题、1->收藏、2->笔记）
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @ApiOperation("设置用户学科问题信息：（错题、收藏、笔记）")
    @PostMapping("/set_user_question_info")
    public Result setUserSubjectQuestionInfo(@RequestParam("id")String id,
                                             @RequestParam("questionId")String questionId,
                                             @RequestParam("option")Integer option,
                                             @RequestParam("state")Integer state){
        service.setUserSubjectQuestionInfo(id, questionId, option,state);
        return Result.success();
    }


    /**
     * @description: 随机查询题目(按学科id)
     * @time: 2023/4/27 12:39
     * @param id 按学科id
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result<java.util.List < com.liyuan.domain.ExamSubject>>
     */
    @ApiOperation("随机查询题目(按学科id)")
    @GetMapping("/random_query")
    public Result<Map> randomQuery(@RequestParam("id")String id){
        Map map = service.randomQuery(id);
        return Result.success(map);
    }

    /**
     * @description: 按天提交答案
     * @time: 2023/4/27 17:50
     * @param answersInfoByDay 题目ID,对应用户回答
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result<java.lang.Object>
     */
    @PostMapping("submit_answers_by_day")
    private Result<Object> submitAnswersByDay(@RequestBody AnswersInfoByDay answersInfoByDay){
        service.submitAnswersByDay(answersInfoByDay);
        return Result.success();
    }

    /**
     * @description: 查询用户答题信息；（错题，收藏，笔记）
     * @time: 2023/4/30 23:48
     * @param id 学科ID
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result<java.lang.Object>
     */
    @GetMapping("userAnswerInfo")
    private Result<Object> getUserAnswerInfo(@RequestParam("id")String id,
                                             @RequestParam("option")Integer option){
        Boolean userAnswerInfo = service.getUserAnswerInfo(id, option);
        return Result.success(userAnswerInfo);
    }

    /**
     * @description: 获取章节Id
     * @time: 2023/5/1 18:52
     * @param id 答题记录ID
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result<java.lang.String>
     */
    @GetMapping("chapterId")
    private Result<String> getChapterId(@RequestParam("id")String id){
        String chapterId = service.getChapterId(id);
        return Result.success(chapterId);
    }



}
