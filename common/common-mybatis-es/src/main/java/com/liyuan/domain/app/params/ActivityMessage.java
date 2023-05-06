package com.liyuan.domain.app.params;

import com.liyuan.domain.Activity;
import lombok.Data;

/**
 * @author liyuan
 * @date 2023/3/16
 * @project exam-cloud
 */
@Data
public class ActivityMessage {

    //用户open_id
    private String touser;

    //模板id
    private String template_id;

    //data
    private Object data;

    private Activity activity;
}
