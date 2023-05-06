package com.liyuan.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyuan.typeHandler.Sex;
import com.liyuan.typeHandler.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
* @author liyuan
*@date 2022/10/15
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-User")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`user`")
public class User implements Serializable {
    /**
     * 用户ID编码
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="用户ID编码")
    private String id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    @ApiModelProperty(value="用户名")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password",select = false)
    @ApiModelProperty(value="密码")
    private String password;

    /**
     * 姓名
     */
    @TableField(value = "name")
    @ApiModelProperty(value="姓名")
    private String name;

    /**
     * 性别
     */
    @TableField(value = "sex",typeHandler = EnumOrdinalTypeHandler.class,javaType = true)
    @ApiModelProperty(value="性别")
    private Sex sex;

    /**
     * 微信唯一标识符
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value="微信唯一标识符")
    private String openId;

    /**
     * 出生日期
     */
    @TableField(value = "birthday")
    @ApiModelProperty(value="出生日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date birthday;

    /**
     * 电话号码
     */
    @TableField(value = "phone")
    @ApiModelProperty(value="电话号码")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
     * 家庭住址
     */
    @TableField(value = "address")
    @ApiModelProperty(value="家庭住址")
    private String address;

    /**
     * 用户头像
     */
    @TableField(value = "head_portrait")
    @ApiModelProperty(value="用户头像")
    private String headPortrait;

    /**
     * 文化程度
     */
    @TableField(value = "education")
    @ApiModelProperty(value="文化程度")
    private String education;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "modified_time",fill=FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="修改时间")
    private Date modifiedTime;


    /**
     * 逻辑删除
     */
    @TableField(value = "is_deleted",fill = FieldFill.INSERT,select = false)
    @TableLogic
    @ApiModelProperty(value="逻辑删除")
    @JsonIgnore
    private Integer isDeleted;

    /**
     * 帐号启用状态：0->禁用；1->启用
     */
    @TableField(value = "status",typeHandler = EnumOrdinalTypeHandler.class,javaType = true)
    @ApiModelProperty(value="帐号启用状态：0->禁用；1->启用")
    private UserStatus status;

    @TableField(exist = false)
    private ArrayList<String> roles;

    @TableField(exist = false)
    private String roleNames;

    @TableField(exist = false)
    private String roleIds;


    private static final long serialVersionUID = 1L;
}