package com.liyuan.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @author liyuan
*@date 2022/10/24
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-RoleMenu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "role_menu")
public class RoleMenu implements Serializable {
    /**
     * 角色ID
     */
    @TableField(value = "role_id")
    @ApiModelProperty(value="角色ID")
    private String roleId;

    /**
     * 菜单ID
     */
    @TableField(value = "menu_id")
    @ApiModelProperty(value="菜单ID")
    private String menuId;

    private static final long serialVersionUID = 1L;
}