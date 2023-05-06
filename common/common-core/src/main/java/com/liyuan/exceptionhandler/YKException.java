package com.liyuan.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liyuan
 * @date 2022/5/30
 * @name cloud_question_bank
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YKException extends RuntimeException {
    @ApiModelProperty(value = "状态码")
    private String code;

    @ApiModelProperty(value = "消息")
    private String msg;

}
