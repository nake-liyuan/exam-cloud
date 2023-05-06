package com.liyuan.domain.app.vo.postings;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author liyuan
 * @date 2023/2/11
 * @project exam-cloud
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostingsVo{

    private String id;
    //高亮用户名
    private String highlightUsername;
    //普通用户名
    private String username;
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss",timezone = "GMT+8")
    private Date releaseDate;
    private String userImage;
    private List<String> label;
    private String desc;
    private List<String> mainImage;

    //1->确定,0->取消
    //踩
    private Integer trampleState;
    private Integer trampleCount;
    //点赞
    private Integer likeState;
    private Integer likeCount;
    //评论
    private Integer commentCount;
    //观看用户
    private HashSet<String> latestViewUserAvatar;
    private Integer viewUserCount;





}
