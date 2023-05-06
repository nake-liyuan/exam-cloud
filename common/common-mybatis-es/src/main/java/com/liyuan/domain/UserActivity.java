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
*@date 2023/2/9
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-UserActivity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_activity")
public class UserActivity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="id")
    private String id;

    /**
     * 活动id
     */
    @TableField(value = "activity_id")
    @ApiModelProperty(value="活动id")
    private String activityId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户id")
    private String userId;

    /**
     * 用户头像
     */
    @TableField(value = "user_image")
    @ApiModelProperty(value="用户头像")
    private String userImage;

    /**
     * 用户状态（0->未参加，1->以参加，1->以打卡）
     */
    @TableField(value = "user_state")
    @ApiModelProperty(value="用户状态（0->未参加，1->以参加，1->以打卡）")
    private Integer userState;

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

    /**
     * 逻辑删除
     */
    @TableField(value = "is_deleted",fill = FieldFill.INSERT)
    @ApiModelProperty(value="逻辑删除")
    @TableLogic
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}