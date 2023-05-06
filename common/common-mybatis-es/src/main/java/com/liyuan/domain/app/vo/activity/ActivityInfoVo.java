package com.liyuan.domain.app.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author liyuan
 * @date 2023/2/5
 * @project exam-cloud
 */
@Data
public class ActivityInfoVo {
    private String id;
    private String laber;
    private String image;
    private String content;
    private String addressName;
    private String detailedAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date activityTime;
    private String location;
    private ArrayList<String> viewUserImage;
    @Value("${some.key:0}")
    private Integer userState;

}
