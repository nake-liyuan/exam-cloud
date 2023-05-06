package com.liyuan.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @author liyuan
*@date 2023/2/2
*@project exam-cloud
*/
@ApiModel(value="com-liyuan-domain-File")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`file`")
public class File implements Serializable {
    /**
     * 文件ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value="文件ID")
    private String id;

    /**
     * 文件名
     */
    @TableField(value = "name")
    @ApiModelProperty(value="文件名")
    private String name;

    /**
     * 文件类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value="文件类型")
    private String type;

    /**
     * 文件url
     */
    @TableField(value = "url")
    @ApiModelProperty(value="文件url")
    private String url;

    /**
     * 文件说明
     */
    @TableField(value = "text")
    @ApiModelProperty(value="文件说明")
    private String text;

    /**
     * 文件大小（16位长度，小数点后2位）
     */
    @TableField(value = "size")
    @ApiModelProperty(value="文件大小（16位长度，小数点后2位）")
    private Double size;

    private static final long serialVersionUID = 1L;
}