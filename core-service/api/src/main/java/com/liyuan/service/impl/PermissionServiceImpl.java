package com.liyuan.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.constant.AuthConstant;
import com.liyuan.domain.Permission;
import com.liyuan.domain.vo.UrlRole;
import com.liyuan.mapper.mp.PermissionMapper;
import com.liyuan.service.PermRolesRulesService;
import com.liyuan.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
* @author liyuan
*@date 2022/10/15
*@project exam-cloud
*/
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermRolesRulesService {

    private final PermissionMapper permissionMapper;
    private final RedisService redisService;

    @Override
    public boolean refreshPermRolesRules() {
        Map<String,List<String>> resourceRoleMap = new TreeMap<>();
        List<UrlRole> urlRoles = permissionMapper.initResourceRolesMap();

        for (UrlRole urlRole : urlRoles) {
            if(urlRole.getRoles()!=null){
                List<String> roleNames=Arrays.asList(urlRole.getRoles().split(","));
                resourceRoleMap.put(urlRole.getUrl(), roleNames);
            }else {
                resourceRoleMap.put(urlRole.getUrl(), null);
            }
        }
        redisService.del(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        redisService.hSetAll(AuthConstant.RESOURCE_ROLES_MAP_KEY, resourceRoleMap);
        return true;
    }


}
