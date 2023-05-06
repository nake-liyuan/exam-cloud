package com.liyuan.domain.app.params;

import com.liyuan.entity.applet.Message;
import lombok.Data;

import java.util.Date;

/**
 * @author liyuan
 * @date 2023/3/16
 * @project exam-cloud
 */
@Data
public class JoinArrival {

    private Message message;

    private String activityId;

    private String userImage;

    private Date date;
}
