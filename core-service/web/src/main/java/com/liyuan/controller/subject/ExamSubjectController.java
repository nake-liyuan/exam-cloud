package com.liyuan.controller.subject;


import cn.hutool.core.lang.tree.Tree;
import com.liyuan.api.Result;
import com.liyuan.domain.ExamSubject;
import com.liyuan.service.ExamSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * @author liyuan
 * @date 2022/10/21
 * @project exam-cloud
 */
@RestController
@RequestMapping("subject")
@RequiredArgsConstructor
@Api(tags = "题库接口")
public class ExamSubjectController {

     public final ExamSubjectService examSubjectService;

    /**
     * @description 题目信息查询（分页）
     * @route GET: /subject/page/**
     */
    @ApiOperation("题目信息查询（分页）")
    @GetMapping("/page/{page}/{size}")
    public Result examSubjectPage(
           @ApiParam("页码") @PathVariable Integer page,
           @ApiParam("条数") @PathVariable Integer size,
           @ApiParam("id") @RequestParam(required = false) String id,
           @ApiParam("题目或答案关键词") @RequestParam(required = false) String name
     ){
        Result result = examSubjectService.listExamSubjectPage(page, size, id, name);
        return result;
     }

    /**
     * @description 学科目录查询
     * @route GET: /subject/catalogue
     */
    @ApiOperation("学科目录查询")
    @GetMapping("/catalogue")
    public Result queryTree(){
        List<Tree<String>> queryTree = examSubjectService.getQueryTree();
        return Result.success(queryTree);
    }

    /**
     * @description 保存学科记录
     * @route POST: /subject
     */
    @ApiOperation("保存学科记录")
    @PostMapping
    public Result save(@RequestBody ExamSubject examSubject){
        boolean save = examSubjectService.save(examSubject);
        return Result.judge(save);
    }

    @ApiOperation("批量导入")
    @PostMapping("/batch")
    public Result batchImport(@RequestBody List<ExamSubject> examSubject){
        Boolean aBoolean = examSubjectService.BatchImport(examSubject);
        return Result.judge(aBoolean);
    }

    /**
     * @description 编辑学科记录
     * @route PUT: /subject
     */
    @ApiOperation("编辑学科记录")
    @PutMapping
    public Result edit(@RequestBody ExamSubject examSubject){
        boolean save = examSubjectService.updateById(examSubject);
        return Result.judge(save);
    }

    /**
     * @description 删除学科记录
     * @route DELETE: /subject/*
     */
    @ApiOperation("删除学科记录")
    @DeleteMapping("/{ids}")
    public Result delete(@ApiParam("学科记录Id") @PathVariable String ids){
        boolean save = examSubjectService.removeByIds(Arrays.asList(ids));
        return Result.judge(save);
    }
}
