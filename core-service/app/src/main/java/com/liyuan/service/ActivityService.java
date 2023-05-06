package com.liyuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.api.Result;
import com.liyuan.domain.Activity;
import com.liyuan.domain.app.params.ActivityParams;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author liyuan
 * @date 2023/2/4
 * @project exam-cloud
 */
@SuppressWarnings("rawtypes")
public interface ActivityService extends IService<Activity> {

    Result listActivityPage(ActivityParams activityParams);

    Result userArrivalLocationSubmit(String id, Double latitude,Double longitude);

    void establishActivity(Activity activity,String avatar);

    void deleteActivity(ArrayList<String> ids);

    void userJoinArrival(String id,String avatar);

    void addActivityBrowseCount(String id);

    HashMap<String,Object> userCreatedActivity();
    HashMap<String,Object> userJoinActivity();
    HashMap<String,Object> userCompleteActivity();

    void signOutActivity(String id);

}
