package com.liyuan.entity.applet;

import lombok.Data;

/**
 * @author liyuan
 * @date 2023/2/8
 * @project exam-cloud
 */
@Data
public class Message {

    //用户open_id
    private String touser;

    //模板id
    private String template_id;

    //data
    private Object data;

}
