package com.liyuan.component;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.liyuan.entity.SecurityAppUser;
import com.liyuan.entity.SecurityUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author liyuan
 * @date 2022/9/9
 * @project exam
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Object principal = authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> additionalInfo = MapUtil.newHashMap();
        if (principal instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) principal;
            //把用户通用信息和客户端Id设置到JWT中
            additionalInfo.put("id", securityUser.getId());
            additionalInfo.put("client_id", securityUser.getClientId());
            // 认证身份标识(username:用户名；)
            if (StrUtil.isNotBlank(securityUser.getAuthenticationIdentity())) {
                additionalInfo.put("authenticationIdentity", securityUser.getAuthenticationIdentity());
            }
        } else if (principal instanceof SecurityAppUser) {
            SecurityAppUser securityAppUser = (SecurityAppUser) principal;
            //把用户通用信息和客户端Id设置到JWT中
            additionalInfo.put("id", securityAppUser.getId());
            additionalInfo.put("client_id", securityAppUser.getClientId());
            // 认证身份标识(mobile:手机号；openId:开放式认证系统唯一身份标识)
            if (StrUtil.isNotBlank(securityAppUser.getAuthenticationIdentity())) {
                additionalInfo.put("authenticationIdentity", securityAppUser.getAuthenticationIdentity());
            }
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
