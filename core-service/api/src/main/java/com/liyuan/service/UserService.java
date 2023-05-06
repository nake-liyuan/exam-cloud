package com.liyuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.api.Result;
import com.liyuan.domain.User;
import com.liyuan.entity.AppUser;
import com.liyuan.entity.AppUserDto;
import com.liyuan.entity.UserDto;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
public interface UserService extends IService<User> {

    Result<UserDto> loadUserByUsername(String username);

    Result<AppUserDto> loadUserByPhone(String phone);

    Result<AppUserDto> loadUserByOpenId(String openId);

    boolean appUserRegister(AppUser user);
}
