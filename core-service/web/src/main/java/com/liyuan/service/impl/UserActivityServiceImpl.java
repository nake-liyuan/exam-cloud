package com.liyuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.UserActivity;
import com.liyuan.mapper.mp.UserActivityMapper;
import com.liyuan.service.UserActivityService;
import org.springframework.stereotype.Service;
/**
* @author liyuan
*@date 2023/2/9
*@project exam-cloud
*/
@Service
public class UserActivityServiceImpl extends ServiceImpl<UserActivityMapper, UserActivity> implements UserActivityService{

}
