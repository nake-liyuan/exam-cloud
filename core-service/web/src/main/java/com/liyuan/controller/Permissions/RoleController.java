package com.liyuan.controller.Permissions;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuan.api.ApiClientFeignClient;
import com.liyuan.api.Result;
import com.liyuan.domain.Role;
import com.liyuan.domain.params.RoleMenuPermission;
import com.liyuan.domain.vo.menuPermission.MenuPermission;
import com.liyuan.result.PageResult;
import com.liyuan.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liyuan
 * @date 2022/11/6
 * @project exam-cloud
 */
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
@Api(tags = "角色接口")
public class RoleController {

    private final RoleService roleService;
    private final ApiClientFeignClient apiClientFeignClient;

    /**
     * @description 系统角色查询
     * @route GET: /role/page/**
     */
    @ApiOperation("系统角色查询")
    @GetMapping("/page/{page}/{size}")
    public PageResult rolePage(
            @ApiParam("页码") @PathVariable Integer page,
            @ApiParam("条数") @PathVariable Integer size,
            @ApiParam("角色名") @RequestParam(required = false,value = "roleNameZh") String roleNameZh
    ){
        Page<Role> rolePage = roleService.rolePage(page, size, roleNameZh);
        return PageResult.success(rolePage);
    }

    /**
     * @description 角色子菜单查询
     * @route GET: /role/childLevelMenu
     */
    @ApiOperation("角色子菜单查询")
    @GetMapping("/childLevelMenu")
    public Result roleChildLevelMenu(
            @ApiParam("角色id") @RequestParam(value = "id") String id
    ){
        String[] menuId = roleService.roleChildLevelMenu(id);
        return Result.success(menuId);
    }

    /**
     * @description 角色资源查询
     * @route GET: /role/resources
     */
    @ApiOperation("角色资源查询")
    @GetMapping("/resources")
    public Result roleMenuPermission(
            @ApiParam("角色Id") @RequestParam(value = "id") String id,
            @ApiParam("菜单名") @RequestParam(value = "menuId") String menuId
    ){
        List<MenuPermission> menuPermissions = roleService.roleMenuPermission(id, menuId);
        return Result.success(menuPermissions);
    }

    /**
     * @description 新增角色记录
     * @route POST: /role
     */
    @ApiOperation("新增角色")
    @PostMapping
    public Result saveRole(@RequestBody Role role){
        boolean save = roleService.save(role);
        return Result.judge(save);
    }

    /**
     * @description 编辑菜单记录
     * @route PUT: /role
     */
    @ApiOperation("编辑角色")
    @PutMapping
    public Result editRole( @RequestBody Role role){
        boolean edit = roleService.updateById(role);
        return Result.judge(edit);
    }

    /**
     * @description 角色资源分配
     * @route PUT: /role/distributionPermission
     */
    @ApiOperation("角色资源分配")
    @PutMapping("/distributionPermission")
    public Result distributionPermission( @RequestBody RoleMenuPermission roleMenuPermission){
        boolean edit = roleService.distributionPermission(roleMenuPermission);
        if(edit){
            apiClientFeignClient.refreshPermRolesRules();
        }
        return Result.judge(edit);
    }

    /**
     * @description 删除角色记录
     * @route DELETE: /role
     */
    @ApiOperation("删除角色")
    @DeleteMapping
    public Result deleteRole(@ApiParam("角色Id") @RequestParam ArrayList<String> ids){
        boolean delete = roleService.removeByIds(ids);
        return Result.judge(delete);
    }


}
