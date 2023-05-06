package com.liyuan.controller;

import cn.hutool.json.JSONObject;
import com.liyuan.api.Result;
import com.liyuan.constant.AuthConstant;
import com.liyuan.domain.User;
import com.liyuan.domain.app.vo.postings.CirclePointsRanking;
import com.liyuan.entity.dto.UserOtherInfo;
import com.liyuan.service.UserService;
import com.liyuan.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author liyuan
 * @date 2022/10/15
 * @project exam-cloud
 */
@SuppressWarnings({"unchecked","rawtypes"})
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final RedisTemplate redisTemplate;

    /**
     * @description 获取小程序用户信息
     */
    @ApiOperation(value = "获取小程序用户信息")
    @GetMapping("/me")
    public Result<User> getCurrMemberInfo() {
        User userInfo = userService.getUserInfo();
        return Result.success(userInfo);
    }

    @ApiOperation(value = "获取用户其它信息")
    @GetMapping("/other_info")
    public Result<User> getUserOtherInfo() {
        User user = userService.getuserOtherInfo();
        return Result.success(user);
    }

    @ApiOperation(value = "设置用户其它信息")
    @PutMapping("/other_info")
    public Result<User> setUserOtherInfo(@RequestBody UserOtherInfo userOtherInfo) {
        userService.setUserOtherInfo(userOtherInfo);
        return Result.success();
    }

    @ApiOperation(value = "获取用户页基本信息")
    @PostMapping("/info")
    public Result<HashMap<String, Long>> getUserPageBasicInfo(@RequestBody CirclePointsRanking circlePointsRanking) {
        HashMap<String, Long> userPageBasicInfo = userService.getUserPageBasicInfo(circlePointsRanking);
        return Result.success(userPageBasicInfo);
    }

    @DeleteMapping("/logout")
    public Result logout() {
        JSONObject payload = JwtUtils.getJwtPayload();
        String jti = payload.getStr("jti"); // JWT唯一标识
        Long expireTime = payload.getLong("exp"); // JWT过期时间戳(单位：秒)
        if (expireTime != null) {
            long currentTime = System.currentTimeMillis() / 1000;// 当前时间（单位：秒）
            if (expireTime > currentTime) { // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
                redisTemplate.opsForValue().set(AuthConstant.TOKEN_BLACKLIST_PREFIX + jti, null, (expireTime - currentTime), TimeUnit.SECONDS);
            }
        } else { // token 永不过期则永久加入黑名单
            redisTemplate.opsForValue().set(AuthConstant.TOKEN_BLACKLIST_PREFIX + jti, null);
        }
        return Result.success("注销成功");
    }


}
