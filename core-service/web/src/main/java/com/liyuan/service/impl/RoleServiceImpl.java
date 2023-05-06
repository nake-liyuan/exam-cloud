package com.liyuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuan.domain.Role;
import com.liyuan.domain.RoleMenu;
import com.liyuan.domain.RolePermission;
import com.liyuan.domain.params.RoleMenuPermission;
import com.liyuan.domain.vo.menuPermission.MenuPermission;
import com.liyuan.domain.vo.menuPermission.MenuPermissionKeyValue;
import com.liyuan.mapper.mp.MenuMapper;
import com.liyuan.mapper.mp.RoleMapper;
import com.liyuan.service.RoleMenuService;
import com.liyuan.service.RolePermissionService;
import com.liyuan.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author liyuan
*@date 2022/10/16
*@project exam-cloud
*/
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService{

    private final RoleMapper roleMapper;
    private final MenuMapper menuMapper;
    private final RoleMenuService roleMenuService;
    private final RolePermissionService rolePermissionService;

    @Override
    public Page<Role> rolePage(Integer page, Integer size, String roleNameZh) {
        Page<Role> rolePage = roleMapper.selectPage(
                new Page<Role>(page, size),
                new QueryWrapper<Role>().eq(StringUtils.hasText(roleNameZh), "role_name_zh", roleNameZh)
        );
        return rolePage;
    }

    @Override
    public List<MenuPermission> roleMenuPermission(String id, String menuId) {
        ArrayList<String> menusId = menuMapper.listMenusId(menuId);
        List<MenuPermission> menuPermissions = roleMapper.listMenuPermissionById(menusId);
        List<MenuPermissionKeyValue> menuPermissionKeyValues = roleMapper.permissionValueByRoleId(id);
        List<MenuPermission> collect = menuPermissions.stream().map(e -> {
                for (MenuPermissionKeyValue menuPermissionKeyValue : menuPermissionKeyValues) {
                    if(e.getMenu().equals(menuPermissionKeyValue.getTitle())){
                        e.setRolePermission(menuPermissionKeyValue.getPermissionId());
                    }
                }
                if(e.getRolePermission()==null){
                    e.setRolePermission(new ArrayList<>());
                }
            return e;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public String[] roleChildLevelMenu(String id) {
        String[] menuId = roleMapper.roleChildLevelMenu(id);
        return menuId;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean distributionPermission(RoleMenuPermission roleMenuPermission) {
        if(!roleMenuPermission.getDeleteMenuIds().isEmpty()){
            roleMenuService.remove(
                    new QueryWrapper<RoleMenu>()
                            .in("menu_id",roleMenuPermission.getDeleteMenuIds())
                            .eq("role_id",roleMenuPermission.getId())
            );
        }
        if(!roleMenuPermission.getAddMenuIds().isEmpty()){
            List<RoleMenu> addmenus = roleMenuPermission.getAddMenuIds().stream().map(e -> {
                RoleMenu roleMenu = new RoleMenu(roleMenuPermission.getId(),e);
                return roleMenu;
            }).collect(Collectors.toList());
            roleMenuService.saveBatch(addmenus);
        }
        if(!roleMenuPermission.getDeletepermissionIds().isEmpty()){
            rolePermissionService.remove(
                    new QueryWrapper<RolePermission>()
                            .in("permission_id",roleMenuPermission.getDeletepermissionIds())
                            .eq("role_id",roleMenuPermission.getId())
            );
        }
        if(!roleMenuPermission.getAddPermissionIds().isEmpty()){
            List<RolePermission> addPermission = roleMenuPermission.getAddPermissionIds().stream().map(e -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleMenuPermission.getId());
                rolePermission.setPermissionId(e);
                return rolePermission;
            }).collect(Collectors.toList());
            rolePermissionService.saveBatch(addPermission);
        }
        return true;
    }
}
