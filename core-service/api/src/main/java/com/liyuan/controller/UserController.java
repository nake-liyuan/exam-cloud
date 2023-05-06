package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.entity.AppUser;
import com.liyuan.entity.AppUserDto;
import com.liyuan.entity.UserDto;
import com.liyuan.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * @description 根据用户名获取通用用户信息
     * @route GET: /loadByUsername
     */
    @ApiOperation(value = "根据用户名获取通用用户信息",hidden = true)
    @GetMapping("/loadByUsername")
    public Result<UserDto> loadByUsername(
            @ApiParam("用户名") @RequestParam String username) {
        Result<UserDto> userDtoResult = userService.loadUserByUsername(username);
        return userDtoResult;
    }

    /**
     * @description 根据phone获取通用用户信息
     * @route GET: /loadByPhone
     */
    @ApiOperation(value = "根据phone获取通用用户信息", hidden = true)
    @GetMapping("/loadByPhone")
    public Result<AppUserDto> loadByPhone(
            @ApiParam("用户名") @RequestParam String phone) {
        Result<AppUserDto> result = userService.loadUserByPhone(phone);
        return result;
    }


    /**
     * @description 根据openId获取通用用户信息
     * @route GET: /loadByOpenId
     */
    @ApiOperation(value = "根据openId获取通用用户信息", hidden = true)
    @GetMapping("/loadByOpenId")
    public Result<AppUserDto> loadByOpenId(@ApiParam("openId")@RequestParam String openId){
        Result<AppUserDto> appUserDtoResult = userService.loadUserByOpenId(openId);
        return appUserDtoResult;
    }

    /**
     * @description 通过Wechat信息注册小程序用户
     * @route GET: /appUserRegister
     */
    @ApiOperation(value = "通过Wechat信息注册小程序用户", hidden = true)
    @PostMapping("/appUserRegister")
    public Result appUserRegister(@RequestBody AppUser user){
        boolean save = userService.appUserRegister(user);
        return Result.judge(save);
    }



}
