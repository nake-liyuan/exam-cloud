package com.liyuan.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
* @author liyuan
*@date 2023/2/14
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-UserState")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_state")
public class UserState implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="id")
    private String id;

    /**
     * 用户id
     */
    @TableField(value = "uid")
    @ApiModelProperty(value="用户id")
    private String uid;

    /**
     * 关注用户id
     */
    @TableField(value = "follow_uid")
    @ApiModelProperty(value="关注用户id")
    private String followUid;

    /**
     * 帖子，圈子id
     */
    @TableField(value = "postings_id")
    @ApiModelProperty(value="帖子，圈子id")
    private String postingsId;

    /**
     * 点赞状态
     */
    @TableField(value = "like_state")
    @ApiModelProperty(value="点赞状态")
    private Integer likeState;

    /**
     * 踩状态
     */
    @TableField(value = "trample_state")
    @ApiModelProperty(value="踩状态")
    private Integer trampleState;

    /**
     * 观看状态
     */
    @TableField(value = "view_state")
    @ApiModelProperty(value="观看状态")
    private Integer viewState;

    /**
     * 圈子关注状态
     */
    @TableField(value = "circle_follow_state")
    @ApiModelProperty(value="圈子关注状态")
    private Integer circleFollowState;

    /**
     * 用户关注状态,0->没有关注,1->关注了
     */
    @TableField(value = "user_follow_state")
    @ApiModelProperty(value="用户关注状态")
    private Integer userFollowState;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "modified_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="修改时间")
    private Date modifiedTime;

    private static final long serialVersionUID = 1L;
}