package com.liyuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.Activity;
import com.liyuan.mapper.mp.ActivityMapper;
import com.liyuan.service.ActivityService;
import org.springframework.stereotype.Service;
/**
* @author liyuan
*@date 2023/2/4
*@project exam-cloud
*/
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService{

}
