package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.entity.UserDto;
import com.liyuan.service.PermRolesRulesService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liyuan
 * @date 2023/3/10
 * @project exam-cloud
 */
@RestController
@RequestMapping("permRolesRules")
@RequiredArgsConstructor
public class permRolesRulesController {

    private final PermRolesRulesService permRolesRulesService;

    /**
     * @description 初始化权限角色缓存
     * @route POST: /permRolesRules
     */
    @ApiOperation(value = "初始化权限角色缓存",hidden = true)
    @PostMapping("/refreshPermRolesRules")
    public Result<UserDto> refreshPermRolesRules() {
        permRolesRulesService.refreshPermRolesRules();
        return Result.success();
    }
}
