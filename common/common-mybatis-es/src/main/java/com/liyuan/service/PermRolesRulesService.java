package com.liyuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.domain.Permission;
import org.springframework.stereotype.Service;

/**
* @author liyuan
*@date 2022/10/15
*@project exam-cloud
*/
@Service
public interface PermRolesRulesService extends IService<Permission>{

    boolean refreshPermRolesRules();

}
