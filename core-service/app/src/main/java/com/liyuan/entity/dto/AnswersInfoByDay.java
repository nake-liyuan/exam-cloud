package com.liyuan.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: LiYuan
 * @time: 2023/4/27 17:30
 */
@Data
public class AnswersInfoByDay {

    private List<String> problemId;

    private List<String> userAnswers;

}
