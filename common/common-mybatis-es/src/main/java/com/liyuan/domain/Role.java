package com.liyuan.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(value="com-liyuan-domain-Role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`role`")
public class Role implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="主键ID")
    private String id;

    /**
     * 角色名称
     */
    @TableField(value = "role_name")
    @ApiModelProperty(value="角色名称")
    private String roleName;

    /**
     * 角色中文名称
     */
    @TableField(value = "role_name_zh")
    @ApiModelProperty(value="角色中文名称")
    private String roleNameZh;

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
    @TableField(value = "is_deleted",fill = FieldFill.INSERT,select = false)
    @ApiModelProperty(value="逻辑删除")
    @TableLogic
    @JsonIgnore
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}