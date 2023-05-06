package com.liyuan.domain.app.vo.postings;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author liyuan
 * @date 2023/2/20
 * @project exam-cloud
 */
@Data
public class CommentVo {

    private String id;

    private Boolean owner;

    private Boolean hasLike;

    private Integer likeNum;

    private String avatarUrl;

    private String nickName;

    private String content;

    private String parentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

}
