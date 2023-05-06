package com.liyuan.domain.app.vo.postings;

import lombok.Data;

import java.util.List;

/**
 * @author liyuan
 * @date 2023/2/14
 * @project exam-cloud
 */
@Data
public class CircleInfoVo {

    private List<String> swiperList;

    private String mainImage;

    private String name;

    private String desc;

    private Integer followState;
}
