package com.liyuan.service.impl;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.api.ApiClientFeignClient;
import com.liyuan.api.AuthClientFeignClient;
import com.liyuan.constant.AuthConstant;
import com.liyuan.domain.User;
import com.liyuan.domain.UserRole;
import com.liyuan.domain.vo.RoleParams;
import com.liyuan.domain.vo.UserInfo;
import com.liyuan.mapper.mp.RoleMapper;
import com.liyuan.mapper.mp.UserMapper;
import com.liyuan.service.UserRoleService;
import com.liyuan.service.UserService;
import com.liyuan.util.JwtUtils;
import com.liyuan.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author liyuan
 * @date 2022/10/15
 * @project exam-cloud
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleService userRoleService;
    private final RedisTemplate redisTemplate;
    private final AuthClientFeignClient authClientFeignClient;

    @Override
    public HashMap<String,String> getUserInfo() {
        String userId = UserUtils.getUserId();
        String authorities = UserUtils.getAuthorities();
        String username = UserUtils.getUsername();
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("id", userId)
                .select("name", "head_portrait", "phone")
        );
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("id",userId);
        userInfo.put("username",username);
        userInfo.put("nickname",user.getName());
        userInfo.put("avatar",user.getHeadPortrait());
        userInfo.put("phone",user.getPhone());
        userInfo.put("roleName",authorities);
        return userInfo;
    }

    @Override
    public List<RoleParams> roleParams() {
        List<RoleParams> roleParams = roleMapper.roleParams();
        return roleParams;
    }

    @Override
    public Page<UserInfo> listUserPage(Integer page, Integer size, String value, Integer status) {
        Page<User> userPage = userMapper.listUser(new Page<User>(page, size), value, status);
        List<UserInfo> collect = userPage.getRecords().stream()
                .map(e -> {
                    UserInfo userInfo = new UserInfo();
                    BeanUtils.copyProperties(e, userInfo);
                    userInfo.setRoleName(e.getRoleNames().split(","));
                    userInfo.setRoleId(e.getRoleIds().split(","));
                    userInfo.setMap(StringUtils.hasText(e.getAddress())?e.getAddress().split(","):new String[0]);
                    return userInfo;

                }).collect(Collectors.toList());
        Page<UserInfo> userInfoPage = new Page<>();
        userInfoPage.setRecords(collect);
        userInfoPage.setTotal(userPage.getTotal());
        return userInfoPage;
    }

    @Transactional
    @Override
    public void setAdminAccountInfo(User user) {
        user.setPassword(authClientFeignClient.getBCrypt(user.getPassword()));
        this.updateById(user);
    }

    @Override
    public void setOwnAccountInfo(User user) {
        System.out.println(user);
        if(StringUtils.hasText(user.getPassword())){
            user.setPassword(authClientFeignClient.getBCrypt(user.getPassword()));
        }
        this.updateById(user);
    }


    @Override
    public boolean editUser(UserInfo userInfo) {
        User user = new User();
        BeanUtils.copyProperties(userInfo, user);
        StringBuffer sb = new StringBuffer();
        if (userInfo.getMap()!=null&&userInfo.getMap().length>0){
            for (int i = 0; i < userInfo.getMap().length; i++) {
                sb.append(userInfo.getMap()[i]);
                if(i!=2){
                    sb.append(",");
                }
            }
        }

        user.setAddress(sb.toString());
        boolean updateById = this.updateById(user);
        if(updateById){
            boolean remove = userRoleService.remove(
                    new QueryWrapper<UserRole>()
                    .eq("user_id", user.getId()));
            if(remove){
                ArrayList<UserRole> userRoleArrayList = new ArrayList<>();
                for (String s : userInfo.getRoleId()) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(s);
                    userRoleArrayList.add(userRole);
                }
                boolean save = this.userRoleService.saveBatch(userRoleArrayList);
                return  save;
            }
            return remove;
        }

        return updateById;
    }

    @Override
    public boolean delete(ArrayList<String> ids) {
        boolean delete = this.removeByIds(ids);
        if(delete){
            boolean user_id = userRoleService.remove(new QueryWrapper<UserRole>().in("user_id", ids));
            return user_id;
        }
        return delete;
    }

    @Override
    public boolean logout() {
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
        return true;
    }
}
