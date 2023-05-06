package com.liyuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.api.Result;
import com.liyuan.api.ResultCode;
import com.liyuan.domain.User;
import com.liyuan.domain.vo.UserVo;
import com.liyuan.entity.AppUser;
import com.liyuan.entity.AppUserDto;
import com.liyuan.entity.UserDto;
import com.liyuan.mapper.mp.UserMapper;
import com.liyuan.service.UserService;
import com.liyuan.typeHandler.Sex;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
@Service
@RequiredArgsConstructor
public class UsreServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;


    @Override
    public Result<UserDto> loadUserByUsername(String username) {
        UserVo userRoles = userMapper.getUserRoles(username);
        if (userRoles != null) {
            UserDto userDto = new UserDto(
                    userRoles.getId(),
                    userRoles.getUsername(),
                    userRoles.getPassword(),
                    userRoles.getStatus(),
                    null,
                    userRoles.getRoles()
            );
            return Result.success(userDto);
        } else {
            return Result.success();
        }
    }

    @Override
    public Result<AppUserDto> loadUserByPhone(String phone) {
        User user = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("phone", phone)
                        .select("id","phone","status")
        );
        if (user!=null){
            AppUserDto appUserDto = new AppUserDto(user.getId(),user.getPhone(),user.getStatus().getType(),null);
            return Result.success(appUserDto);
        }else {
            return Result.failed(ResultCode.USER_NOT_EXIST);
        }

    }

    @Override
    public Result<AppUserDto> loadUserByOpenId(String openId) {
        User user = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("open_id", openId)
                        .select("id", "open_id", "status")
        );
        if(user!=null){
            AppUserDto appUserDto = new AppUserDto(user.getId(),user.getOpenId(),user.getStatus().getType(),null);
            return Result.success(appUserDto);
        }else {
            return Result.failed(ResultCode.USER_NOT_EXIST);
        }


    }

    @Override
    public boolean appUserRegister(AppUser appUser) {
        Sex sex = appUser.getGender().equals("0") ? Sex.FEMALE : Sex.MALE;
        User user = new User();
        user.setName(appUser.getNickName());
        user.setSex(sex);
        user.setOpenId(appUser.getOpenId());
        user.setHeadPortrait(appUser.getAvatarUrl());
        boolean save = this.save(user);
        return save;
    }
}
