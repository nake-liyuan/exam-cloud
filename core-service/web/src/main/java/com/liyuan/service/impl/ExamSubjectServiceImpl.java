package com.liyuan.service.impl;


import cn.easyes.core.biz.PageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.api.Result;
import com.liyuan.domain.ExamSubject;
import com.liyuan.domain.File;
import com.liyuan.mapper.ee.EsExamSubjectMapper;
import com.liyuan.mapper.mp.ExamSubjectMapper;
import com.liyuan.service.ExamSubjectService;
import com.liyuan.typeHandler.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author liyuan
*@date 2022/10/21
*@project exam-cloud
*/
@Service
@RequiredArgsConstructor
public class ExamSubjectServiceImpl extends ServiceImpl<ExamSubjectMapper, ExamSubject> implements ExamSubjectService{

    public final ExamSubjectMapper examSubjectMapper;

    public final EsExamSubjectMapper esExamSubjectMapper;

    @Override
    public Result listExamSubjectPage(Integer pageNum, Integer pageSize,String id,String name) {
        if (StringUtils.hasText(id)) {
            List<String> topicPid = examSubjectMapper.getTopicPid(id);
            LambdaEsQueryWrapper<ExamSubject> wrapper = new LambdaEsQueryWrapper<>();
            wrapper.multiMatchQuery(
                    StringUtils.hasText(name), name, ExamSubject::getName,
                    ExamSubject::getOptionA, ExamSubject::getOptionB,
                    ExamSubject::getOptionC, ExamSubject::getOptionD
            ).eq("type", Type.SUBJECT.getType()).in("pid", topicPid).eq("is_deleted", 0);
            PageInfo<ExamSubject> documentPageInfo = esExamSubjectMapper.pageQuery(wrapper, pageNum, pageSize);
            Map<String ,Object> map=new HashMap<>();
            map.put("total",documentPageInfo.getTotal());
            map.put("list",documentPageInfo.getList());
            return Result.success(map);
        } else {
            LambdaEsQueryWrapper<ExamSubject> wrapper = new LambdaEsQueryWrapper<>();
            wrapper.multiMatchQuery(
                    StringUtils.hasText(name), name, ExamSubject::getName,
                    ExamSubject::getOptionA, ExamSubject::getOptionB,
                    ExamSubject::getOptionC, ExamSubject::getOptionD
            ).eq("type", Type.SUBJECT.getType()).eq("is_deleted", 0);
            PageInfo<ExamSubject> documentPageInfo = esExamSubjectMapper.pageQuery(wrapper, pageNum, pageSize);
            Map<String ,Object> map=new HashMap<>();
            map.put("total",documentPageInfo.getTotal());
            map.put("list",documentPageInfo.getList());
            return Result.success(map);

        }
    }

    @Override
    public List<Tree<String>> getQueryTree() {
        List<ExamSubject> examSubjects = examSubjectMapper.selectList(
                new QueryWrapper<ExamSubject>().ne("type",4)
        );
        List<Tree<String>> treeList = TreeUtil.build(examSubjects, "0",
                new TreeNodeConfig()
                        .setIdKey("value")
                        .setDeep(5)
                        .setWeightKey("sort"),
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setWeight(treeNode.getSort());
                    // 扩展属性 ...
                    tree.putExtra("label", treeNode.getName());
                    tree.putExtra("sort", treeNode.getSort()+1);
                    tree.putExtra("type", treeNode.getType());
                }
        );
        return treeList;
    }

    @Override
    public Boolean BatchImport(List<ExamSubject> examSubjectList) {
        boolean saveBatch = this.saveBatch(examSubjectList);
        return saveBatch;
    }



}
