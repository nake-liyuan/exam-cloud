package com.liyuan.domain.app.vo;

import lombok.Data;

/**
 * @author liyuan
 * @date 2023/2/16
 * @project exam-cloud
 */
@Data
public class BloggerInfoVo {

    private String id;

    private String userName;

    private String userImage;

    private String phone;

    private String email;

    private Integer likeNum;
    //积分
    private Double score;
    //粉丝
    private Long fansNum;
    //关注
    private Long followNum;
    //是否关注用户
    private Boolean followUser;
}
