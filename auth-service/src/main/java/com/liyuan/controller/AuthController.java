package com.liyuan.controller;

import com.liyuan.api.Result;
import com.liyuan.constant.AuthConstant;
import com.liyuan.entity.Oauth2TokenDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.text.ParseException;
import java.util.Map;

/**
 * @author liyuan
 * @date 2022/9/9
 * @project exam
 */
@Api(tags = "认证中心登录认证")
@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenEndpoint tokenEndpoint;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation("Oauth2获取token")
    @PostMapping(value = "/token")
    public Result postAccessToken(
            @ApiIgnore Principal principal,
            @ApiIgnore @RequestParam Map<String, String> parameters
    ) throws HttpRequestMethodNotSupportedException, ParseException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal,parameters).getBody();
        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead(AuthConstant.JWT_TOKEN_PREFIX).build();
        return Result.success(oauth2TokenDto);
    }

    @ApiOperation("加密密码")
    @GetMapping("/getBCrypt")
    @ApiIgnore
    public String getBCrypt(@RequestParam("password") String password){
        return passwordEncoder.encode(password);
    }

}
