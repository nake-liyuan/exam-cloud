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
*@date 2022/10/31
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-AnswerRecord")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "answer_record")
public class AnswerRecord implements Serializable {

    /**
     * 用户回答记录ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="证书ID")
    private String id;
    /**
     * 学科ID
     */
    @TableField(value = "subject_id")
    @ApiModelProperty(value="答题记录ID")
    private String subjectId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户ID")
    private String userId;

    /**
     * 答题数
     */
    @TableField(value = "answer_num")
    @ApiModelProperty(value="答题数")
    private Integer answerNum;

    /**
     * 正确数
     */
    @TableField(value = "correct_num")
    @ApiModelProperty(value="正确数")
    private Integer correctNum;

    /**
     * 用户答案
     */
    @TableField(value = "user_answer")
    @ApiModelProperty(value="用户答案")
    private String userAnswer;

    /**
     * 用户笔记
     */
    @TableField(value = "topic_note")
    @ApiModelProperty(value="用户笔记")
    private String topicNote;

    /**
     * 是否记录在错题集
     */
    @TableField(value = "is_mistakes")
    @ApiModelProperty(value="是否记录在错题集")
    private Integer isMistakes;

    /**
     * 是否收藏
     */
    @TableField(value = "is_collect")
    @ApiModelProperty(value="是否收藏")
    private Integer isCollect;

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
    @TableField(value = "is_deleted",select = false,fill = FieldFill.INSERT)
    @ApiModelProperty(value="逻辑删除")
    @JsonIgnore
    @TableLogic
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}