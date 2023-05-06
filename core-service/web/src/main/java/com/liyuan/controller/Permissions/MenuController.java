package com.liyuan.controller.Permissions;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuan.api.Result;
import com.liyuan.domain.Menu;
import com.liyuan.domain.Permission;
import com.liyuan.result.PageResult;
import com.liyuan.service.MenuService;
import com.liyuan.service.PermissionService;
import com.liyuan.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liyuan
 * @date 2022/10/24
 * @project exam-cloud
 */
@RestController
@RequestMapping("menu")
@RequiredArgsConstructor
@Api(tags = "菜单接口")
public class MenuController {

    private final MenuService menuService;
    private final PermissionService permissionService;
    private final RedisService redisService;
    /**
     * @description 角色菜单获取
     * @route GET: /menu/byRoles
     */
    @ApiOperation("角色菜单获取")
    @GetMapping("/byRoles")
    public Result getMenuByRole(@ApiParam("角色名") @RequestParam ArrayList<String> roles){
        List<Tree<String>> menuByRole = menuService.getMenuByRole(roles);
        return Result.success(menuByRole);
    }

    /**
     * @description 菜单目录查询
     * @route GET: /menu/catalogue
     */
    @ApiOperation("菜单目录查询")
    @GetMapping("/catalogue")
    public Result listMenuCatalogue(){
        List<Tree<String>> treeList = menuService.listMenuCatalogue();
        return Result.success(treeList);
    }

    /**
     * @description 系统菜单查询
     * @route GET: /menu
     */
    @ApiOperation("系统菜单查询")
    @GetMapping
    public Result listMenu( @ApiParam("菜单标题") @RequestParam(required = false,value = "title") String title){
        List<Tree<String>> Tree = menuService.listMenu(title);
        return Result.success(Tree);
    }

    /**
     * @description 保存菜单记录
     * @route POST: /menu
     */
    @ApiOperation("保存菜单记录")
    @PostMapping
    public Result saveMenu( @RequestBody Menu menu){
        boolean save = menuService.save(menu);
        return Result.judge(save);
    }

    /**
     * @description 编辑菜单记录
     * @route PUT: /menu
     */
    @ApiOperation("编辑菜单记录")
    @PutMapping
    public Result editMenu( @RequestBody Menu menu){
        boolean edit = menuService.updateById(menu);
        return Result.judge(edit);
    }

    /**
     * @description 删除菜单记录
     * @route DELETE: /menu/**
     */
    @ApiOperation("删除菜单记录")
    @DeleteMapping
    public Result deleteMenu( @RequestParam ArrayList<String> ids){
        boolean delete = menuService.removeByIds(ids);
        return Result.judge(delete);
    }

    /**
     * @description 查询指定菜单权限
     * @route GET: /menu/permission/page/**
     */
    @ApiOperation("查询菜单权限（分页）")
    @GetMapping("/permission/page/{page}/{size}")
    public PageResult listMenuPermissionPage(
                                      @ApiParam("页码") @PathVariable Integer page,
                                      @ApiParam("条数") @PathVariable Integer size,
                                      @ApiParam("Id") @RequestParam(required = false) String id,
                                      @ApiParam("权限名") @RequestParam(required = false) String name){
        Page<Permission> PermissionPage = menuService.listPermissionByMenuId(page, size, id, name);
        return PageResult.success(PermissionPage);
    }


    /**
     * @description 新增权限
     * @route POST: /menu/permission
     */
    @ApiOperation("新增权限记录")
    @PostMapping("/permission")
    public Result savePermission( @RequestBody Permission permission){
        boolean save = permissionService.save(permission);
        return Result.judge(save);
    }

    /**
     * @description 编辑权限记录
     * @route PUT: /menu/permission
     */
    @ApiOperation("编辑权限记录")
    @PutMapping("permission")
    public Result editPermission( @RequestBody Permission permission){
        boolean edit = permissionService.updateById(permission);
        return Result.judge(edit);
    }

    /**
     * @description 删除权限记录
     * @route DELETE: /menu/permission/**
     */
    @ApiOperation("删除权限记录")
    @DeleteMapping("permission")
    public Result deletePermission( @RequestParam ArrayList<String> ids){
        boolean delete = permissionService.removeByIds(ids);
        return Result.judge(delete);
    }


}
