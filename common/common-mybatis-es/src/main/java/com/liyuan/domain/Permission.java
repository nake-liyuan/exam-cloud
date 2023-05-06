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
*@date 2022/10/15
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-Permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "permission")
public class Permission implements Serializable {
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 菜单模块ID
     */
    @TableField(value = "menu_id")
    @ApiModelProperty(value="父级权限id")
    private Long menuId;

    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value="名称")
    private String name;

    /**
     * 权限值
     */
    @TableField(value = "value")
    @ApiModelProperty(value="权限值")
    private String value;

    /**
     * 权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）
     */
    @TableField(value = "type")
    @ApiModelProperty(value="权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）")
    private Integer type;

    /**
     * 前端资源路径
     */
    @TableField(value = "uri")
    @ApiModelProperty(value="前端资源路径")
    private String uri;

    /**
     * 逻辑删除
     */
    @TableField(value = "is_deleted",fill = FieldFill.INSERT,select = false)
    @ApiModelProperty(value="启用状态；0->禁用；1->启用")
    @TableLogic
    @JsonIgnore
    private Integer isDeleted;

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