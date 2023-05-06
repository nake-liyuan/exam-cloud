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
*@date 2023/2/11
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-Postings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "postings")
@IndexName(value = "exam_cloud_postings", shardsNum = 1, replicasNum = 0)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Postings implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="id")
    private String id;

    /**
     * 父id
     */
    @IndexField(fieldType = FieldType.KEYWORD,value = "pid")
    @TableField(value = "pid")
    @ApiModelProperty(value="父id")
    private String pid;

    /**
     * 父id
     */
    @IndexField(fieldType = FieldType.KEYWORD,value = "uid")
    @TableField(value = "uid")
    @ApiModelProperty(value="用户id")
    private String uid;

    /**
     * 用户名
     */
    @IndexField(fieldType = FieldType.TEXT,analyzer = "ik_max_word",value = "username")
    @HighLight(preTag = "<span style='color:#FD463E;'>",postTag = "</span>",mappingField = "highlightUsername")
    @TableField(value = "username")
    @ApiModelProperty(value="用户名")
    private String username;

    /**
     * 高亮用户名
     */
    @TableField(exist = false)
    private String highlightUsername;

    /**
     * 用户头像
     */
    @IndexField(fieldType = FieldType.KEYWORD,value = "user_image")
    @TableField(value = "user_image")
    @ApiModelProperty(value="用户头像")
    private String userImage;

    /**
     * 圈子名称
     */
    @IndexField(fieldType = FieldType.TEXT,analyzer = "ik_max_word",value = "circle_name")
    @HighLight(preTag = "<span style='color:#FD463E;'>",postTag = "</span>")
    @TableField(value = "circle_name")
    @ApiModelProperty(value="圈子名称")
    private String circleName;

    /**
     * 主图
     */
    @IndexField(fieldType = FieldType.KEYWORD,value = "main_image")
    @TableField(value = "main_image")
    @ApiModelProperty(value="主图")
    private String mainImage;

    /**
     * 标签
     */
    @IndexField(fieldType = FieldType.TEXT,analyzer = "ik_max_word",value = "label")
    @TableField(value = "label")
    @ApiModelProperty(value="标签")
    private String label;

    /**
     * 描述
     */
    @IndexField(fieldType = FieldType.TEXT,analyzer = "ik_max_word",value = "desc")
    @HighLight(preTag = "<span style='color:#FD463E;'>",postTag = "</span>")
    @TableField(value = "`desc`")
    @ApiModelProperty(value="描述")
    private String desc;

    /**
     * 内容
     */
    @IndexField(fieldType = FieldType.KEYWORD,value = "content")
    @TableField(value = "content")
    @ApiModelProperty(value="内容")
    private String content;

    /**
     * 圈子关注数
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "follow_count")
    @TableField(value = "follow_count",fill = FieldFill.INSERT)
    @ApiModelProperty(value="圈子关注数")
    private Integer followCount;

    /**
     * 点赞数
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "like_count")
    @TableField(value = "like_count",fill = FieldFill.INSERT)
    @ApiModelProperty(value="点赞数")
    private Integer likeCount;

    /**
     * 踩数
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "trample_count")
    @TableField(value = "trample_count",fill = FieldFill.INSERT)
    @ApiModelProperty(value="踩数")
    private Integer trampleCount;

    /**
     * 观看数
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "view_count")
    @TableField(value = "view_count",fill = FieldFill.INSERT)
    @ApiModelProperty(value="观看数")
    private Integer viewCount;

    /**
     * 评论数
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "comment_count")
    @TableField(value = "comment_count",fill = FieldFill.INSERT)
    @ApiModelProperty(value="评论数")
    private Integer commentCount;

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
    @TableField(value = "is_deleted",fill = FieldFill.INSERT,select = false)
    @ApiModelProperty(value="逻辑删除")
    @TableLogic
    @JsonIgnore
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}