package com.liyuan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liyuan
 * @date 2022/10/19
 * @project exam-cloud
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToEmail {
    /**
     * 邮件接收方，可多人
     */
    private String[] tos;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
}
