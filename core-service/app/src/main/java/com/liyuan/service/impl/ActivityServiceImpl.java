package com.liyuan.service.impl;

import cn.easyes.common.constants.BaseEsConstants;
import cn.easyes.core.biz.SAPageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.api.Result;
import com.liyuan.domain.Activity;
import com.liyuan.domain.UserActivity;
import com.liyuan.domain.app.params.ActivityParams;
import com.liyuan.domain.app.vo.activity.ActivityInfoVo;
import com.liyuan.mapper.ee.EsActivityMapper;
import com.liyuan.mapper.mp.ActivityMapper;
import com.liyuan.mapper.mp.UserActivityMapper;
import com.liyuan.service.ActivityService;
import com.liyuan.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liyuan
 * @date 2023/2/4
 * @project exam-cloud
 */
@Slf4j
@SuppressWarnings({"unchecked","rawtypes"})
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    private final ActivityMapper activityMapper;
    private final EsActivityMapper esActivityMapper;
    private final UserActivityMapper userActivityMapper;

    @Override
    public Result listActivityPage(ActivityParams activityParams) {
        if (activityParams.getId() != null) {
            ActivityInfoVo activityInfoVo = new ActivityInfoVo();
            Activity activity = esActivityMapper.selectOne(new LambdaEsQueryWrapper<Activity>()
                    .select(Activity::getId, Activity::getLaber, Activity::getImage, Activity::getContent,
                            Activity::getAddressName, Activity::getDetailedAddress,Activity::getActivityTime, Activity::getLocation)
                    .eq(Activity::getId, activityParams.getId())
            );
            BeanUtils.copyProperties(activity, activityInfoVo);
            List<UserActivity> userActivities = userActivityMapper.selectList(
                    new QueryWrapper<UserActivity>()
                            .select("user_image","user_state","user_id")
                            .eq("activity_id",activityParams.getId())
            );
            ArrayList<String> userImages = new ArrayList<>();
            userActivities.forEach(e -> {
                userImages.add(e.getUserImage());
                if (UserUtils.getUserId().equals(e.getUserId())) {
                    activityInfoVo.setUserState(e.getUserState());
                }
            });
            activityInfoVo.setViewUserImage(userImages);
            if(activityInfoVo.getUserState()==null){
                activityInfoVo.setUserState(0);
            }
            return Result.success(activityInfoVo);
        } else {
            SAPageInfo<Activity> saPageInfo = esActivityMapper.searchAfterPage(
                    new LambdaEsQueryWrapper<Activity>()
                            .select(Activity::getId, Activity::getTitle, Activity::getLaber, Activity::getImage,
                                    Activity::getAttendUserCount, Activity::getViewUserCount, Activity::getAddressName)
                            .multiMatchQuery(
                                    StringUtils.hasText(activityParams.getValue()), activityParams.getValue(),
                                    Activity::getTitle, Activity::getLaber, Activity::getAddressName
                            )
                            .geoDistance(activityParams.getDistanceQueryStatus(), Activity::getLocation, activityParams.getDistance(), DistanceUnit.KILOMETERS, new GeoPoint(activityParams.getLatitude(), activityParams.getLongitude()), BaseEsConstants.DEFAULT_BOOST)
                            .and(activityParams.getSorOrder() == 0, activityLambdaEsQueryWrapper -> activityLambdaEsQueryWrapper
                                    .orderByDesc(Activity::getCreateTime, Activity::getAttendUserCount, Activity::getViewUserCount)
                            )
                            .and(activityParams.getSorOrder() == 1, activityLambdaEsQueryWrapper -> activityLambdaEsQueryWrapper
                                    .orderByDesc(Activity::getAttendUserCount, Activity::getCreateTime, Activity::getViewUserCount)
                            )
                            .and(activityParams.getSorOrder() == 2, activityLambdaEsQueryWrapper -> activityLambdaEsQueryWrapper
                                    .orderByDesc(Activity::getViewUserCount, Activity::getCreateTime, Activity::getAttendUserCount)
                            )
                            .and(activityParams.getSorOrder() == 3, activityLambdaEsQueryWrapper -> activityLambdaEsQueryWrapper
                                    .orderByDistanceAsc(Activity::getLocation, new GeoPoint(activityParams.getLatitude(), activityParams.getLongitude()))
                            )

                    ,
                    activityParams.getNextSearchAfter(), activityParams.getPageSize());
            List<Activity> collect = saPageInfo.getList().stream().peek(e -> e.setImage(e.getImage().split(",")[0])).collect(Collectors.toList());
            saPageInfo.setList(collect);
            return Result.success(saPageInfo);
        }
    }

    @Override
    public Result userArrivalLocationSubmit(String id,Double latitude, Double longitude) {
        Activity activity = esActivityMapper.selectOne(
                new LambdaEsQueryWrapper<Activity>()
                        .select(Activity::getId)
                        .geoDistance(Activity::getLocation, 1.0, new GeoPoint(latitude, longitude))
                        .eq(Activity::getId, id)
        );
        if(activity!=null){
            UserActivity userActivity = new UserActivity();
            userActivity.setUserState(2);
            userActivityMapper.update(userActivity,
                    new UpdateWrapper<UserActivity>()
                            .set("user_state",2)
                            .eq("activity_id",id)
                            .eq("user_id",UserUtils.getUserId())
                    );
            return Result.success(true);
        }else {
            return Result.success(false);
        }

    }

    @SneakyThrows
    @Override
    public void userJoinArrival(String id, String avatar) {
        UserActivity userActivity = new UserActivity();
        userActivity.setUserId(UserUtils.getUserId());
        userActivity.setActivityId(id);
        userActivity.setUserImage(avatar);
        userActivity.setUserState(1);
        int insert = userActivityMapper.insert(userActivity);
        if(insert==1){
            this.update(new UpdateWrapper<Activity>()
                    .eq("id", id)
                    .setSql("attend_user_count = attend_user_count + 1"));
        }
    }

    @Override
    public void addActivityBrowseCount(String id) {
            this.update(new UpdateWrapper<Activity>().eq("id",id).setSql("view_user_count = view_user_count + 1"));
    }

    @Override
    public HashMap<String, Object> userCreatedActivity() {
        List<Activity> activities = esActivityMapper.selectList(new LambdaEsQueryWrapper<Activity>()
                .eq(Activity::getUid, UserUtils.getUserId())
                .eq(Activity::getIsDeleted,0)
                .select(Activity::getId,Activity::getTitle,Activity::getAttendUserCount,Activity::getImage)
        );
        return createActivityIndexList(activities);
    }

    @Override
    public HashMap<String, Object> userJoinActivity() {
        String uid=UserUtils.getUserId();
        List<UserActivity> userActivityList = userActivityMapper.selectList(new QueryWrapper<UserActivity>()
                .select("activity_id")
                .eq("user_id", uid)
                .eq("user_state",1)
        );
        List<Activity> establishActivityList = this.activityMapper.selectList(new QueryWrapper<Activity>().eq("uid",UserUtils.getUserId()).select("id"));
        List<String> ids1 = userActivityList.stream().map(e -> e.getActivityId()).collect(Collectors.toList());
        List<String> ids2 = establishActivityList.stream().map(e -> e.getId()).collect(Collectors.toList());
        ids1.removeAll(ids2);
        if (ids1.isEmpty()){
            HashMap<String, Object> info = new HashMap<>();
            info.put("indexList",new ArrayList<>());
            info.put("list",new ArrayList<>());
            return info;
        }
        List<Activity> activities = activityMapper.selectList(new QueryWrapper<Activity>()
                .in("id", ids1)
                .select("id", "title", "attend_user_count", "image")
        );
        return createActivityIndexList(activities);
    }

    @Override
    public HashMap<String, Object> userCompleteActivity() {
        String uid=UserUtils.getUserId();
        List<UserActivity> userActivityList = userActivityMapper.selectList(new QueryWrapper<UserActivity>()
                .select("activity_id")
                .eq("user_id", uid)
                .eq("user_state",2)
        );
        List<String> ids = userActivityList.stream().map(e -> e.getActivityId()).collect(Collectors.toList());
        if (ids.isEmpty()){
            HashMap<String, Object> info = new HashMap<>();
            info.put("indexList",new ArrayList<>());
            info.put("list",new ArrayList<>());
            return info;
        }
        List<Activity> activities = activityMapper.selectList(new QueryWrapper<Activity>()
                .in("id", ids)
                .select("id", "title", "attend_user_count", "image")
        );
        return createActivityIndexList(activities);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signOutActivity(String id) {
        this.userActivityMapper.delete(new QueryWrapper<UserActivity>()
                .eq("activity_id",id)
                .eq("user_id",UserUtils.getUserId())
        );
        this.update(new UpdateWrapper<Activity>()
                .eq("id", id)
                .setSql("attend_user_count = attend_user_count - 1"));
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void establishActivity(Activity activity, String avatar) {
        activity.setViewUserCount(0);
        boolean save = this.save(activity);
        if(save){
            UserActivity userActivity = new UserActivity();
            userActivity.setUserId(UserUtils.getUserId());
            userActivity.setUserImage(avatar);
            userActivity.setActivityId(activity.getId());
            userActivity.setUserState(1);
            userActivityMapper.insert(userActivity);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteActivity(ArrayList<String> ids) {
        boolean remove = this.removeByIds(ids);
        if(remove){
            userActivityMapper.delete(
                    new QueryWrapper<UserActivity>()
                            .in("activity_id",ids)
            );
        }
    }

    private  HashMap<String, Object> createActivityIndexList(List<Activity> activities){
        HashSet<String> indexList = new HashSet();
        HashMap<String, List<Activity>> map = new HashMap<>();
        activities.forEach(e->{
            String firstLetter = PinyinUtil.getFirstLetter(e.getTitle().substring(0, 1), "");
            if(map.containsKey(firstLetter)){
                List<Activity> activityList = map.get(firstLetter);
                activityList.add(e);
                map.put(firstLetter,activityList);
            }else {
                indexList.add(PinyinUtil.getFirstLetter(e.getTitle().substring(0,1), ""));
                List<Activity> initActivityList = new ArrayList<>();
                initActivityList.add(e);
                map.put(firstLetter,initActivityList);
            }
        });
        List<HashMap<String, Object>> collect = indexList.stream().map(e -> {
            HashMap<String, Object> data = new HashMap<>();
            data.put("letter", e);
            data.put("data", map.get(e));
            return data;
        }).collect(Collectors.toList());

        HashMap<String, Object> info = new HashMap<>();
        info.put("indexList",indexList);
        info.put("list",collect);
        return info;
    }

}
