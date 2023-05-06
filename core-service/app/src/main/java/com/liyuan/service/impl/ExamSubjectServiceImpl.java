package com.liyuan.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.api.ResultCode;
import com.liyuan.constant.AuthConstant;
import com.liyuan.domain.AnswerRecord;
import com.liyuan.domain.ExamSubject;
import com.liyuan.domain.app.vo.ChapterInfoVo;
import com.liyuan.domain.app.vo.UserSubjectInfo;
import com.liyuan.entity.dto.AnswersInfoByDay;
import com.liyuan.exceptionhandler.YKException;
import com.liyuan.mapper.mp.ExamSubjectMapper;
import com.liyuan.service.AnswerRecordService;
import com.liyuan.service.ExamSubjectService;
import com.liyuan.service.RedisService;
import com.liyuan.typeHandler.QuestionType;
import com.liyuan.typeHandler.Type;
import com.liyuan.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author liyuan
 * @date 2022/11/24
 * @project exam-cloud
 */
@Service
@SuppressWarnings({"unchecked","rawtypes"})
@RequiredArgsConstructor
public class ExamSubjectServiceImpl extends ServiceImpl<ExamSubjectMapper, ExamSubject> implements ExamSubjectService {

    private final ExamSubjectMapper subjectMapper;
    private final AnswerRecordService answerRecordService;
    private final RedisTemplate redisTemplate;

    private final RedisService redisService;

    @Override
    public List<Tree<String>> getExamSubjectOption() {
        List<ExamSubject> SubjectOption = subjectMapper.selectList(
                new QueryWrapper<ExamSubject>()
                        .in("type", Type.CATEGORY.getType(), Type.NAME.getType())
                        .select("id", "pid", "name", "sort")
        );
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("value");
        // 最大递归深度
        treeNodeConfig.setDeep(3);
        treeNodeConfig.setWeightKey("sort");
        //转换器
        List<Tree<String>> SubjectName = TreeUtil.build(SubjectOption, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setWeight(treeNode.getSort());
                    // 扩展属性 ...
                    tree.putExtra("label", treeNode.getName());
                }
        );
        return SubjectName;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Tree<String>> getChapterInfo(String id) {
        String userId=UserUtils.getUserId();
        Boolean member = redisTemplate.opsForSet().isMember(AuthConstant.USER_SUBJECT_CODE_PREFIX + userId, id);
        redisTemplate.boundValueOps(AuthConstant.USER_CURRENT_SUBJECT_CODE_PREFIX + userId).set(id);
        if (Boolean.FALSE.equals(member)) {
            redisTemplate.opsForSet().add(AuthConstant.USER_SUBJECT_CODE_PREFIX + userId, id);
            List<String> topicId = subjectMapper.getTopicId(id);
            List<ExamSubject> topicInfoList = subjectMapper.selectList(new QueryWrapper<ExamSubject>()
                    .in("id", topicId)
                    .select("DISTINCT pid")
            );
            List<AnswerRecord> SectionInfo = topicInfoList.stream().map(e -> {
                AnswerRecord answerRecord = new AnswerRecord();
                answerRecord.setSubjectId(e.getPid());
                answerRecord.setUserId(userId);
                answerRecord.setAnswerNum(0);
                answerRecord.setCorrectNum(0);
                return answerRecord;
            }).collect(Collectors.toList());
            boolean InitSectionInfo = answerRecordService.saveBatch(SectionInfo);
            if (InitSectionInfo) {
                List<AnswerRecord> answerInfo = topicId.stream().map(e -> {
                    AnswerRecord answerRecord = new AnswerRecord();
                    answerRecord.setSubjectId(e);
                    answerRecord.setUserId(userId);
                    answerRecord.setUserAnswer("");
                    answerRecord.setIsMistakes(0);
                    answerRecord.setIsCollect(0);
                    answerRecord.setAnswerNum(0);
                    answerRecord.setCorrectNum(0);
                    return answerRecord;
                }).collect(Collectors.toList());
                boolean InitAnswerInfo = answerRecordService.saveBatch(answerInfo);
                if (!InitAnswerInfo) {
                    throw new YKException(ResultCode.SYSTEM_RESOURCE_ERROR.getCode(), ResultCode.SYSTEM_RESOURCE_ERROR.getMsg());
                }
            } else {
                throw new YKException(ResultCode.SYSTEM_RESOURCE_ERROR.getCode(), ResultCode.SYSTEM_RESOURCE_ERROR.getMsg());
            }
        }
        List<ExamSubject> examSubjectInfo = subjectMapper.getExamSubjectInfo(id);
        ArrayList<String> ChapterIds = new ArrayList<>();
        examSubjectInfo.forEach(e -> {
            if (e.getType().getType() == Type.SECTION.getType()) {
                ChapterIds.add(e.getId());
            }
        });
        List<AnswerRecord> answerRecords = answerRecordService.list(
                new QueryWrapper<AnswerRecord>()
                        .in("subject_id", ChapterIds)
                        .eq("user_id", userId)
                        .select("subject_id", "answer_num", "correct_num")
        );

        List<ChapterInfoVo> chapterInfoVoList = examSubjectInfo.stream().map(e -> {
                    ChapterInfoVo chapterInfoVo = new ChapterInfoVo();
                    if (id.equals(e.getPid())) {
                        e.setPid("0");
                    }
                    answerRecords.forEach(item -> {
                        if (e.getId().equals(item.getSubjectId())) {
                            chapterInfoVo.setAnswerNum(item.getAnswerNum());
                            chapterInfoVo.setCorrectNum(item.getCorrectNum());
                        }
                    });
                    chapterInfoVo.setNum(e.getNum());
                    chapterInfoVo.setId(e.getId());
                    chapterInfoVo.setName(e.getName());
                    chapterInfoVo.setPid(e.getPid());
                    chapterInfoVo.setSort(e.getSort());
                    return chapterInfoVo;
                }
        ).collect(Collectors.toList());
        //转换器
        List<Tree<String>> chapterInfo = TreeUtil.build(chapterInfoVoList, "0",
                new TreeNodeConfig()
                        .setIdKey("value")
                        .setDeep(2)
                        .setWeightKey("sort"),
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getPid());
                    tree.setWeight(treeNode.getSort());
                    // 扩展属性 ...
                    tree.putExtra("label", treeNode.getName());
                    tree.putExtra("num", treeNode.getNum());
                    tree.putExtra("answerNum", treeNode.getAnswerNum());
                    tree.putExtra("correctNum", treeNode.getCorrectNum());
                }
        );
        return chapterInfo;
    }

    @Override
    public List<UserSubjectInfo> getsubjectInfoBySection(String id) {
        List<UserSubjectInfo> userSubjectInfos = subjectMapper.getsubjectInfoBySection(id, UserUtils.getUserId());
        List<UserSubjectInfo> subjectInfo = OptionDisorder(userSubjectInfos);
        return subjectInfo;
    }

    @Override
    public List<UserSubjectInfo> getWrongQuestionSet(String id) {
        ArrayList<String> ids = new ArrayList<String>((HashSet)redisService.sMembers(AuthConstant.USER_SUBJECT_WRONG_ID+UserUtils.getUserId()+":"+id));
        List<UserSubjectInfo> subjectInfo = OptionDisorder(subjectMapper.getQuestionInfo(ids, UserUtils.getUserId()));
        return subjectInfo;
    }

    @Override
    public List<UserSubjectInfo> getFavorites(String id) {
        ArrayList<String> ids = new ArrayList<String>((HashSet)redisService.sMembers(AuthConstant.USER_SUBJECT_COLLECTION_ID+UserUtils.getUserId()+":"+id));
        List<UserSubjectInfo> subjectInfo = OptionDisorder(subjectMapper.getQuestionInfo(ids, UserUtils.getUserId()));
        return subjectInfo;
    }

    @Override
    public List<UserSubjectInfo> getNotepad(String id) {
        ArrayList<String> ids = new ArrayList<String>((HashSet)redisService.sMembers(AuthConstant.USER_SUBJECT_NOTE_ID+UserUtils.getUserId()+":"+id));
        List<UserSubjectInfo> subjectInfo = OptionDisorder(subjectMapper.getQuestionInfo(ids, UserUtils.getUserId()));
        return subjectInfo;
    }

    @Override
    public Map<String,Object> randomQuery(String id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String key = AuthConstant.USER_EVERYDAY_ANSWER_ID + UserUtils.getUserId()+ ":" + LocalDate.now().format(formatter);
        HashMap<String, Object> result = new HashMap<>();
        if (redisService.hasKey(key)) {
            Map<Object, Object> userAnswersMap = redisService.hGetAll(key);
            Map<String, String> formatMap = userAnswersMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey().toString(),
                            e -> e.getValue().toString()));
            List<String> idList=new ArrayList<>(formatMap.keySet());
            List<ExamSubject> questionInfo = subjectMapper.selectList(new QueryWrapper<ExamSubject>()
                    .in("id", idList));
            List<String> ids = questionInfo.stream().map(ExamSubject::getId).collect(Collectors.toList());
            List<String> valuesList = new ArrayList<>();
            ids.forEach(e->{
                valuesList.add(formatMap.get(e));
            });
            result.put("questionInfo",questionInfo);
            result.put("answerInfo",valuesList);
        } else {
            List<ExamSubject> examSubjects = subjectMapper.randomQuery(id);
            result.put("questionInfo",examSubjects);
            result.put("answerInfo",null);
        }
        return result;
    }

    @Override
    public void submitAnswersByDay(AnswersInfoByDay answersInfoByDay) {
        System.out.println(answersInfoByDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String key=AuthConstant.USER_EVERYDAY_ANSWER_ID+UserUtils.getUserId()+":"+LocalDate.now().format(formatter);
//      将用户每日答题的题目Id和用户答案转为map
        Map<String, String> map =
                IntStream.range(0, answersInfoByDay.getProblemId().size())
                        .boxed()
                        .collect(Collectors.toMap(answersInfoByDay.getProblemId()::get,
                                answersInfoByDay.getUserAnswers()::get,
                                (oldValue, newValue) -> newValue));
        redisService.hSetAll(key,map);
    }

    @Override
    public String getUserCurrentSubject(String key) {
        return (String) redisTemplate.boundValueOps(key).get();
    }

    @Override
    public void setUserSubjectQuestionInfo(String id, String questionId,Integer option,Integer state) {
        if(state==1){
            switch (option){
                case 0:
                    System.out.println("存");
                    redisService.sAdd(AuthConstant.USER_SUBJECT_WRONG_ID+UserUtils.getUserId()+":"+id,questionId);
                    break;
                case 1:
                    redisService.sAdd(AuthConstant.USER_SUBJECT_COLLECTION_ID+UserUtils.getUserId()+":"+id,questionId);
                    break;
                case 2:
                    redisService.sAdd(AuthConstant.USER_SUBJECT_NOTE_ID+UserUtils.getUserId()+":"+id,questionId);
                    break;
            }
        }else {
            switch (option){
                case 0:
                    redisService.sRemove(AuthConstant.USER_SUBJECT_WRONG_ID+UserUtils.getUserId()+":"+id,questionId);
                    break;
                case 1:
                    redisService.sRemove(AuthConstant.USER_SUBJECT_COLLECTION_ID+UserUtils.getUserId()+":"+id,questionId);
                    break;
                case 2:
                    redisService.sRemove(AuthConstant.USER_SUBJECT_NOTE_ID+UserUtils.getUserId()+":"+id,questionId);
                    break;
            }
        }

    }

    @Override
    public Boolean getUserAnswerInfo(String id,Integer option) {
        Boolean hasKey = null;
        switch (option){
            case 0:
                hasKey = redisService.hasKey(AuthConstant.USER_SUBJECT_WRONG_ID + UserUtils.getUserId() + ":" + id);
                break;
            case 1:
                hasKey = redisService.hasKey(AuthConstant.USER_SUBJECT_COLLECTION_ID+UserUtils.getUserId()+":"+id);
                break;
            case 2:
                hasKey = redisService.hasKey(AuthConstant.USER_SUBJECT_NOTE_ID+UserUtils.getUserId()+":"+id);
                break;
        }
        return hasKey;
    }

    @Override
    public String getChapterId(String id) {
        String subjectId = answerRecordService.getOne(new QueryWrapper<AnswerRecord>().select("subject_id").eq("id", id)).getSubjectId();
        String chapterId = subjectMapper.selectOne(new QueryWrapper<ExamSubject>().select("pid").eq("id", subjectId)).getPid();
        return chapterId;
    }

    private List<UserSubjectInfo> OptionDisorder(List<UserSubjectInfo> userSubjectInfos){
        List<String> initialOption = new ArrayList<>();
        List<UserSubjectInfo> subjectInfo = userSubjectInfos.stream().map(e -> {
            if (QuestionType.SINGLE.getValue().equals(e.getQuestionType().getValue())) {
                initialOption.add(e.getOptionA());
                initialOption.add(e.getOptionB());
                initialOption.add(e.getOptionC());
                initialOption.add(e.getOptionD());
                Collections.shuffle(initialOption);
                e.setOptionA(initialOption.get(0));
                e.setOptionB(initialOption.get(1));
                e.setOptionC(initialOption.get(2));
                e.setOptionD(initialOption.get(3));
                initialOption.clear();
            }
            return e;
        }).collect(Collectors.toList());
        return subjectInfo;
    }


}
