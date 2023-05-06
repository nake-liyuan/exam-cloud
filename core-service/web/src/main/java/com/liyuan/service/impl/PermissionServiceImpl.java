package com.liyuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.Permission;
import com.liyuan.mapper.mp.PermissionMapper;
import com.liyuan.service.PermissionService;
import org.springframework.stereotype.Service;

/**
 * @author liyuan
 * @date 2022/11/7
 * @project exam-cloud
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
}
