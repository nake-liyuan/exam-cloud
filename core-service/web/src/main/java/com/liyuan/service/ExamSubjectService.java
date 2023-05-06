package com.liyuan.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.api.Result;
import com.liyuan.domain.ExamSubject;
import com.liyuan.domain.File;

import java.util.List;

/**
* @author liyuan
*@date 2022/10/21
*@project exam-cloud
*/
public interface ExamSubjectService extends IService<ExamSubject>{

    Result listExamSubjectPage(Integer pageNum, Integer pageSize, String id, String name);

    List<Tree<String>> getQueryTree();

    Boolean BatchImport(List<ExamSubject> examSubjectList);

}
