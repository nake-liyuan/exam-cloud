package com.liyuan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liyuan.domain.Role;
import com.liyuan.domain.params.RoleMenuPermission;
import com.liyuan.domain.vo.menuPermission.MenuPermission;

import java.util.List;

/**
* @author liyuan
*@date 2022/10/16
*@project exam-cloud
*/
public interface RoleService extends IService<Role>{

    Page<Role> rolePage(Integer page, Integer size, String name);

    List<MenuPermission> roleMenuPermission(String id, String menuId);

    String[] roleChildLevelMenu(String id);

    Boolean distributionPermission(RoleMenuPermission roleMenuPermission);

}
