package com.liyuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.UserState;
import com.liyuan.mapper.mp.UserStateMapper;
import com.liyuan.service.UserStateService;
import org.springframework.stereotype.Service;
/**
* @author liyuan
*@date 2023/2/14
*@project exam-cloud
*/
@Service
public class UserStateServiceImpl extends ServiceImpl<UserStateMapper, UserState> implements UserStateService{

}
