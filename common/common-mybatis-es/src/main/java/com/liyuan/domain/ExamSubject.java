package com.liyuan.domain;


import cn.easyes.annotation.HighLight;
import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexName;
import cn.easyes.common.enums.FieldType;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liyuan.typeHandler.QuestionType;
import com.liyuan.typeHandler.Type;
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
*@date 2022/10/21
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-ExamSubject")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "exam_subject")
@IndexName(value = "exam_cloud_exam_subject", shardsNum = 1, replicasNum = 0)
public class ExamSubject implements Serializable {
    /**
     * 证书ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="证书ID")
    private String id;

    /**
     * 父级ID
     */
    @IndexField(fieldType = FieldType.KEYWORD,value = "pid")
    @TableField(value = "pid")
    @ApiModelProperty(value="父级ID")
    private String pid;

    /**
     * 名称
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = "ik_max_word",value = "name")
    @TableField(value = "name")
    @ApiModelProperty(value="名称")
    @HighLight(preTag = "<mark><font color='red'>",postTag = "</font></mark>")
    private String name;

    /**
     * 题目总数
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "num")
    @TableField(value = "num")
    @ApiModelProperty(value="题目总数")
    private Integer num;

    /**
     * 科目类型：0->类别；1->题目;
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "type")
    @TableField(value = "type",typeHandler = EnumOrdinalTypeHandler.class,javaType = true)
    @ApiModelProperty(value="科目类型：0->类别；1->学科;2->章节;3->小节;4->题目;")
    private Type type;

    /**
     * 排序
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "sort")
    @TableField(value = "sort")
    @ApiModelProperty(value="排序")
    private Integer sort;

    /**
     * 选项A
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = "ik_max_word",value = "option_a")
    @TableField(value = "option_a")
    @ApiModelProperty(value="选项A")
    @HighLight(preTag = "<mark><font color='red'>",postTag = "</font></mark>")
    private String optionA;

    /**
     * 选项B
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = "ik_max_word",value = "option_b")
    @TableField(value = "option_b")
    @ApiModelProperty(value="选项B")
    @HighLight(preTag = "<mark><font color='red'>",postTag = "</font></mark>")
    private String optionB;

    /**
     * 选项C
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = "ik_max_word",value = "option_c")
    @TableField(value = "option_c")
    @ApiModelProperty(value="选项C")
    @HighLight(preTag = "<mark><font color='red'>",postTag = "</font></mark>")
    private String optionC;

    /**
     * 选项D
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = "ik_max_word",value = "option_d")
    @TableField(value = "option_d")
    @ApiModelProperty(value="选项D")
    @HighLight(preTag = "<mark><font color='red'>",postTag = "</font></mark>")
    private String optionD;

    /**
     * 描述
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "answers_num")
    @TableField(value = "answers_num")
    @ApiModelProperty(value="答案数量")
    private Integer answersNum;

    /**
     * 参考答案
     */
    @IndexField(fieldType = FieldType.TEXT,value = "reference_answer")
    @TableField(value = "reference_answer")
    @ApiModelProperty(value="参考答案")
    private String referenceAnswer;

    /**
     * 答案解析
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = "ik_max_word",value = "analysis")
    @TableField(value = "analysis")
    @ApiModelProperty(value="答案解析")
    private String analysis;

    /**
     * 得分
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "grade")
    @TableField(value = "grade")
    @ApiModelProperty(value="得分")
    private Long grade;

    /**
     * 题目类型：0->单选题；1->多选题；2->判断题；3->填空题；4->主观题
     */
    @IndexField(fieldType = FieldType.INTEGER,value = "question_type")
    @TableField(value = "question_type")
    @ApiModelProperty(value="题目类型：0->单选题；1->多选题；2->判断题；3->填空题；4->主观题")
    private QuestionType questionType;

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