package com.liyuan.domain.app.vo.postings;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author liyuan
 * @date 2023/2/15
 * @project exam-cloud
 */
@Data
public class CircleVo {

    private String id;
    private String userName;
    private String userImage;
    private String mainImage;
    private String name;
    private Integer count;
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss",timezone = "GMT+8")
    private Date releaseDate;
}
