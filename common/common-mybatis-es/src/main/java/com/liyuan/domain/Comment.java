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
*@date 2023/2/20
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-Comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`comment`")
public class Comment implements Serializable {
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
     * 父id
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value="父id")
    private String parentId;

    /**
     * 用户头像地址
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value="用户头像地址")
    private String avatarUrl;

    /**
     * 用户名
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value="用户名")
    private String nickName;

    /**
     * 内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value="内容")
    private String content;

    /**
     * 内容
     */
    @TableField(value = "like_num")
    @ApiModelProperty(value="点赞数")
    private Integer likeNum;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}