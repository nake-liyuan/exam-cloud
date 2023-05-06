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
*@date 2022/10/16
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-UserRole")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_role")
public class UserRole implements Serializable {
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="")
    private String id;

    @TableField(value = "user_id")
    @ApiModelProperty(value="")
    private String userId;

    @TableField(value = "role_id")
    @ApiModelProperty(value="")
    private String roleId;

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