package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.domain.Activity;
import com.liyuan.domain.app.params.ActivityParams;
import com.liyuan.service.ActivityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author liyuan
 * @date 2023/2/4
 * @project exam-cloud
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public Result activityPage(@RequestBody ActivityParams activityParams){
        return activityService.listActivityPage(activityParams);
    }

    /**
     * @description: 用户参加活动
     * @time: 2023/5/3 2:39
     * @param id 活动ID
     * @param avatar 用户头像
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @PostMapping("/join")
    private Result userJoinActivities(@RequestParam String id,
                                   @RequestParam String avatar){
        activityService.userJoinArrival(id,avatar);
        return Result.success();
    }

    /**
     * @description: 活动打卡
     * @time: 2023/5/3 16:55
     * @param id 活动ID
     * @param latitude 维度
     * @param longitude 经度
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @GetMapping("/register")
    private Result userArrivalRegister(@RequestParam("id") String id,
                                       @RequestParam("latitude") Double latitude,
                                       @RequestParam("longitude") Double longitude){
        return activityService.userArrivalLocationSubmit(id, latitude, longitude);
    }

    /**
     * @description: 创建活动
     * @time: 2023/5/3 16:53
     * @param activity 活动实体
     * @param avatar 用户头像
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @PostMapping("/establish")
    public Result establishActivity(@RequestBody Activity activity,
                                    @RequestParam String avatar){
        activityService.establishActivity(activity,avatar);
        return Result.success();
    }

    /**
     * @description: 活动删除
     * @time: 2023/5/3 16:54
     * @param ids 活动ID集合
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @DeleteMapping
    @ApiOperation(value = "活动删除")
    @SneakyThrows
    public Result deleteActivity(
            @ApiParam("活动id") @RequestParam(value = "ids") ArrayList<String> ids
    ) {
        activityService.deleteActivity(ids);
        return Result.success();
    }

    /**
     * @description:
     * @time: 2023/5/3 16:52
     * @param id 活动id
     * @author: LiYuan
     * @since JDK 8
     * @return com.liyuan.api.Result
     */
    @PutMapping("/activity_browse_count")
    @ApiOperation(value = "添加活动浏览计数")
    public Result addActivityBrowseCount(@ApiParam("活动id") @RequestParam String id) {
        activityService.addActivityBrowseCount(id);
        return Result.success();
    }

    @GetMapping("/establish_activity")
    @ApiOperation(value = "用户创建活动")
    public Result userCreatedActivity() {
        HashMap<String, Object> map = activityService.userCreatedActivity();
        return Result.success(map);
    }

    @GetMapping("/join_activity")
    @ApiOperation(value = "用户参与活动")
    public Result userJoinActivity() {
        HashMap<String, Object> map = activityService.userJoinActivity();
        return Result.success(map);
    }

    @DeleteMapping("/sign_out_activity")
    @ApiOperation(value = "用户退出活动")
    public Result signOutActivity(@RequestParam String id) {
        activityService.signOutActivity(id);
        return Result.success();
    }

    @GetMapping("/complete_activity")
    @ApiOperation(value = "用户完成活动")
    public Result userCompleteActivity() {
        HashMap<String, Object> map = activityService.userCompleteActivity();
        return Result.success(map);
    }
}
