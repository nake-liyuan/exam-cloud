package com.liyuan.extension.refresh;

import com.liyuan.constant.AuthConstant;
import com.liyuan.enums.AuthenticationIdentityEnum;
import com.liyuan.userdetails.UserDetailsServiceImpl;
import com.liyuan.util.IBaseEnum;
import com.liyuan.utils.RequestUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
@NoArgsConstructor
public class PreAuthenticatedUserDetailsService<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {

    private UserDetailsService userDetailsService;

    public PreAuthenticatedUserDetailsService(UserDetailsService userDetailsService) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null.");
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "UserDetailsService must be set");
    }

    @Override
    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        String clientId = RequestUtils.getOAuth2ClientId();
        AuthenticationIdentityEnum authenticationIdentityEnum = IBaseEnum.getEnumByValue(RequestUtils.getAuthenticationIdentity(), AuthenticationIdentityEnum.class);
        if (clientId.equals(AuthConstant.PORTAL_CLIENT_ID)) {
            switch (authenticationIdentityEnum) {
                case MOBILE:
                    return ((UserDetailsServiceImpl)userDetailsService).loadUserByPhone(authentication.getName());
                case OPENID:
                    return ((UserDetailsServiceImpl)userDetailsService).loadByOpenId(authentication.getName());
                default:
                    return userDetailsService.loadUserByUsername(authentication.getName());
            }
        }else if (clientId.equals(AuthConstant.ADMIN_CLIENT_ID)){
            switch (authenticationIdentityEnum) {
                default:
                    return userDetailsService.loadUserByUsername(authentication.getName());
            }
        }
        return null;
    }
}
