package com.liyuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.AnswerRecord;
import com.liyuan.mapper.mp.AnswerRecordMapper;
import com.liyuan.service.AnswerRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author liyuan
*@date 2022/10/31
*@project exam-cloud
*/
@Service
@RequiredArgsConstructor
public class AnswerRecordServiceImpl extends ServiceImpl<AnswerRecordMapper, AnswerRecord> implements AnswerRecordService{


}
