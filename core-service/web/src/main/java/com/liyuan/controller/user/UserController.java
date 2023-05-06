package com.liyuan.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyuan.api.Result;
import com.liyuan.domain.User;
import com.liyuan.domain.vo.RoleParams;
import com.liyuan.domain.vo.UserInfo;
import com.liyuan.result.PageResult;
import com.liyuan.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author liyuan
 * @date 2022/10/15
 * @project exam-cloud
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
@Api(tags = "用户接口")
public class UserController {

    private final UserService userService;

    /**
     * @description 用户信息查询（分页）
     * @route GET: /user/page/**
     */
    @ApiOperation("用户信息查询（分页）")
    @GetMapping("/page/{page}/{size}")
    public PageResult listExamSubject(
            @ApiParam("页码") @PathVariable Integer page,
            @ApiParam("条数") @PathVariable Integer size,
            @ApiParam("搜索值") @RequestParam(required = false) String value,
            @ApiParam("状态") @RequestParam(required = false) Integer status
    ){
        Page<UserInfo> userPage = userService.listUserPage(page, size, value, status);
        return PageResult.success(userPage);
    }


    /**
     * @description 用户可选角色参数
     * @route GET: /user/role
     */
    @ApiOperation("用户可选角色参数")
    @GetMapping("/role")
    public Result roleParams(){
        List<RoleParams> roleParams = userService.roleParams();
        return Result.success(roleParams);
    }

    /**
     * Description: 设置管理员帐户信息
     * date: 2023/4/20 0:26
     * @param user
     * @author: LiYuan
     * @since JDK 8
     */
    @PostMapping("/set_admin_account_info")
    public Result setAdminAccountInfo(@RequestBody User user){
        userService.setAdminAccountInfo(user);
        return Result.success();
    }

    /**
     * Description: 设置自己的帐户信息
     * date: 2023/4/20 2:03
     * @param user
     * @author: LiYuan
     * @since JDK 8
     */
    @PostMapping("/modify_personal_info")
    public Result modifyPersonalInfo(@RequestBody User user){
        userService.setOwnAccountInfo(user);
        return Result.success();
    }

    /**
     * @description 编辑用户
     * @route PUT: /user
     */
    @ApiOperation("编辑用户")
    @PutMapping
    public Result edit(@RequestBody UserInfo userInfo){
        return Result.judge(userService.editUser(userInfo));
    }

    /**
     * @description 用户状态改变
     * @route PUT: /user/status
     */
    @ApiOperation("用户状态改变")
    @PutMapping("/status")
    public Result statusChange(@RequestBody User user){
        return Result.judge(userService.updateById(user));
    }


    /**
     * @description 当前用户基本信息
     * @route GET: /user/info
     */
    @ApiOperation("当前用户基本信息")
    @GetMapping("/info")
    public Result userInfo(){
        HashMap<String, String> userInfo = userService.getUserInfo();
        return Result.success(userInfo);
    }

    /**
     * @description 删除用户
     * @route DELETE: /user
     */
    @ApiOperation("删除用户")
    @DeleteMapping
    public Result delete(@ApiParam("用户Id") @RequestParam ArrayList<String> ids){
        boolean delete = userService.delete(ids);
        return Result.judge(delete);
    }

    /**
     * @description 注销
     * @route GET: /logout
     */
    @ApiOperation(value = "注销")
    @DeleteMapping("/logout")
    public Result logout() {
        userService.logout();
        return Result.success("注销成功");
    }

}
