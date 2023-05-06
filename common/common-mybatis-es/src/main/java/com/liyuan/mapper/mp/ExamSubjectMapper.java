package com.liyuan.mapper.mp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyuan.domain.ExamSubject;
import com.liyuan.domain.app.vo.UserSubjectInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
* @author liyuan
*@date 2022/10/21
*@project exam-cloud
*/
@Mapper
public interface ExamSubjectMapper extends BaseMapper<ExamSubject> {

   List<ExamSubject> getExamSubjectByType(String id);

   List<String> getTopicPid(@Param("id")String id);

   List<String> getTopicId(@Param("id")String id);

   List<ExamSubject> getExamSubjectInfo(String id);

   List<UserSubjectInfo> getsubjectInfoBySection(@Param("id")String id,@Param("userId")String userId);

   List<UserSubjectInfo> getQuestionInfo(@Param("ids") ArrayList<String> id, @Param("userId")String userId);

   List<ExamSubject> randomQuery(@Param("id")String id);
}