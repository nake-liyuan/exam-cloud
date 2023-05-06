package com.liyuan.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.liyuan.typeHandler.Hidden;
import com.liyuan.typeHandler.MenuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

import java.io.Serializable;
import java.util.Date;

/**
* @author liyuan
*@date 2022/10/24
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-Menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "menu")
public class Menu implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="id")
    private String id;

    /**
     * 父菜单ID
     */
    @TableField(value = "pid")
    @ApiModelProperty(value="父菜单ID")
    private String pid;

    /**
     * 前端隐藏
     */
    @TableField(value = "hidden",typeHandler = EnumOrdinalTypeHandler.class,javaType = true)
    @ApiModelProperty(value="前端隐藏")
    private Hidden hidden;

    /**
     * 前端名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value="前端名称")
    private String name;

    /**
     * 路由路径(浏览器地址栏路径)
     */
    @TableField(value = "path")
    @ApiModelProperty(value="路由路径(浏览器地址栏路径)")
    private String path;

    /**
     * 类型(组件或链接)
     */
    @TableField(value = "type")
    @ApiModelProperty(value="类型(组件或链接)")
    private String type;

    /**
     * 菜单名称
     */
    @TableField(value = "title")
    @ApiModelProperty(value="菜单名称")
    private String title;

    /**
     * 前端图标
     */
    @TableField(value = "icon")
    @ApiModelProperty(value="前端图标")
    private String icon;

    /**
     * 图标类型，图片或文字
     */
    @TableField(value = "iconType")
    @ApiModelProperty(value="图标类型，图片或文字")
    private String icontype;

    /**
     * 菜单类型(0=>目录;1=>菜单)排序
     */
    @TableField(value = "menuType",typeHandler = EnumOrdinalTypeHandler.class,javaType = true)
    @ApiModelProperty(value="菜单类型(0=>目录;1=>菜单)")
    private MenuType menuType;

    /**
     * 排序
     */
    @TableField(value = "sort")
    @ApiModelProperty(value="排序")
    private Integer sort;

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