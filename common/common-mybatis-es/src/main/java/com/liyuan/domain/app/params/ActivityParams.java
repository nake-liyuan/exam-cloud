package com.liyuan.domain.app.params;

import lombok.Data;

import java.util.List;

/**
 * @author liyuan
 * @date 2023/2/5
 * @project exam-cloud
 */
@Data
public class ActivityParams {
    //条数
    private Integer pageSize;
    //活动ID
    private String id;
    //搜索值
    private String value;
    //距离
    private Double distance;
    //维度
    private Double latitude;
    //经度
    private Double longitude;
    //是否按距离搜索
    private Boolean distanceQueryStatus;
    //下一个搜索
    private List nextSearchAfter;
    //排序方式
    private Integer sorOrder;
}
