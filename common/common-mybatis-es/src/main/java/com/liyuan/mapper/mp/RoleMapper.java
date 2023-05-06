package com.liyuan.mapper.mp;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liyuan.domain.Role;
import com.liyuan.domain.vo.PermissionKeyValue;
import com.liyuan.domain.vo.RoleParams;
import com.liyuan.domain.vo.menuPermission.MenuPermission;
import com.liyuan.domain.vo.menuPermission.MenuPermissionKeyValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
* @author liyuan
*@date 2022/10/16
*@project exam-cloud
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<PermissionKeyValue> permissionValueByRoles(ArrayList<String> roles);

    List<RoleParams> roleParams();

    List<MenuPermission> listMenuPermissionById(@Param("list") ArrayList<String> ids);

    List<MenuPermissionKeyValue>  permissionValueByRoleId(@Param("id") String id);

    String[] roleChildLevelMenu(String id);
}