package com.liyuan.domain.app.vo;

import lombok.Data;

/**
 * @author liyuan
 * @date 2022/11/24
 * @project exam-cloud
 */
@Data
public class ChapterInfoVo {
    private String id;
    private String pid;
    private String name;
    private Integer num;
    private Integer answerNum;
    private Integer correctNum;
    private Integer sort;
}
