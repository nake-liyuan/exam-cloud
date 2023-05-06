package com.liyuan.domain;

import cn.easyes.annotation.HighLight;
import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexName;
import cn.easyes.common.enums.FieldType;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
* @author liyuan
*@date 2023/2/5
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-Activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "activity")
@IndexName(value = "exam_cloud_activity", shardsNum = 1, replicasNum = 0)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Activity implements Serializable {
    /**
     * 活动id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="活动id")
    private String id;

    /**
     * 用户id
     */
    @IndexField(fieldType = FieldType.KEYWORD,value = "uid")
    @TableField(value = "uid")
    @ApiModelProperty(value="标签")
    private String uid;

    /**
     * 标签
     */
    @IndexField(fieldType = FieldType.TEXT,analyzer = "ik_max_word",value = "laber")
    @TableField(value = "laber")
    @ApiModelProperty(value="标签")
    private String laber;

    /**
     * 标题
     */
    @IndexField(fieldType = FieldType.TEXT,analyzer = "ik_max_word",value = "title")
    @HighLight(preTag = "<span style='color:#FD463E;'>",postTag = "</span>")
    @TableField(value = "title")
    @ApiModelProperty(value="标题")
    private String title;

    /**
     * 图片
     */
    @IndexField(fieldType = FieldType.KEYWORD,value = "image")
    @TableField(value = "image")
    @ApiModelProperty(value="活动图片")
    private String image;

    /**
     * 活动内容
     */
    @IndexField(fieldType = FieldType.TEXT,value = "content")
    @TableField(value = "content")
    @ApiModelProperty(value="活动内容")
    private String content;

    /**
     * 参加用户计数
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "attend_user_count")
    @TableField(value = "attend_user_count",fill = FieldFill.INSERT)
    @ApiModelProperty(value="参加用户计数")
    private Integer attendUserCount;

    /**
     * 查看用户计数
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "view_user_count")
    @TableField(value = "view_user_count",fill = FieldFill.INSERT)
    @ApiModelProperty(value="查看用户计数")
    private Integer viewUserCount;

    /**
     * 活动时间
     */
    @IndexField(fieldType = FieldType.DATE,value = "activity_time")
    @TableField(value = "activity_time")
    @ApiModelProperty(value="活动时间")
    private Date activityTime;

    /**
     * 地址名称
     */
    @IndexField(fieldType = FieldType.TEXT,analyzer = "ik_max_word",value = "address_name")
    @HighLight(preTag = "<span style='color:#FD463E;'>",postTag = "</span>")
    @TableField(value = "address_name")
    @ApiModelProperty(value="地址名称")
    private String addressName;

    /**
     * 详细地址
     */
    @IndexField(fieldType = FieldType.KEYWORD,value = "detailed_address")
    @TableField(value = "detailed_address")
    @ApiModelProperty(value="详细地址")
    private String detailedAddress;

    /**
     * 地理位置信息
     */
    @IndexField(fieldType = FieldType.GEO_POINT,value = "location")
    @TableField(value = "location")
    @ApiModelProperty(value="地理位置信息")
    private String location;



    /**
     * 创建时间
     */
    @IndexField(fieldType = FieldType.DATE,value = "create_time")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @IndexField(fieldType = FieldType.DATE,value = "modified_time")
    @TableField(value = "modified_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="修改时间")
    private Date modifiedTime;

    /**
     * 逻辑删除
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "is_deleted")
    @TableField(value = "is_deleted",select = false,fill = FieldFill.INSERT)
    @ApiModelProperty(value="逻辑删除")
    @JsonIgnore
    @TableLogic
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}