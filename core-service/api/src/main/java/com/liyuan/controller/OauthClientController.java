package com.liyuan.controller;

import cn.hutool.core.lang.Assert;
import com.liyuan.api.Result;
import com.liyuan.domain.OauthClient;
import com.liyuan.service.OauthClientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyuan
 * @date 2022/10/15
 * @project exam-cloud
 */
@RestController
@RequestMapping("/oauth-clients")
@RequiredArgsConstructor
public class OauthClientController {

    private final OauthClientService oauthClientService;

    @ApiOperation(value = "获取 OAuth2 客户端认证信息", notes = "Feign 调用", hidden = true)
    @GetMapping("/getOAuth2ClientById")
    public Result<OauthClient> getOAuth2ClientById(
            @ApiParam("客户端ID") @RequestParam String clientId) {
        OauthClient client = oauthClientService.getById(clientId);
        Assert.isTrue(client != null, "OAuth2 客户端不存在");
        return Result.success(client);
    }


}
