package com.liyuan.domain.app.params;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liyuan
 * @date 2023/2/12
 * @project exam-cloud
 */
@Data
public class PostingsParams {

    private Integer orderState;

    private String value;

    private ArrayList<String> pid;

    private List nextSearchAfter;

    private Integer pageSize;

    private Boolean searchMyCircle;

    private Boolean searchMyPostings;
}
