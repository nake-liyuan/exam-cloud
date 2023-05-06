package com.liyuan.userdetails;

import com.liyuan.api.ApiFeignClient;
import com.liyuan.api.Result;
import com.liyuan.constant.MessageConstant;
import com.liyuan.entity.AppUserDto;
import com.liyuan.entity.SecurityAppUser;
import com.liyuan.entity.SecurityUser;
import com.liyuan.entity.UserDto;
import com.liyuan.enums.AuthenticationIdentityEnum;
import com.liyuan.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author liyuan
 * @date 2022/9/10
 * @project exam
 **/
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ApiFeignClient apiFeignClient;

    /**
     * @description 用户名认证方式
     * @param username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<UserDto> result = apiFeignClient.loadUserByUsername(username);
        SecurityUser securityUser=null;
        if (Result.isSuccess(result)) {
            UserDto userInfo = result.getData();
            if (null != userInfo) {
                userInfo.setClientId(RequestUtils.getOAuth2ClientId());
                securityUser = new SecurityUser(userInfo);
                securityUser.setAuthenticationIdentity(AuthenticationIdentityEnum.USERNAME.getValue());   // 认证身份标识:mobile
            }else {
                throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
            }
        }
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
        return securityUser;
    }

    /**
     * @description 手机号码认证方式
     * @param phone
     * @return
     */
    public UserDetails loadUserByPhone(String phone)  {
        Result<AppUserDto> result = apiFeignClient.loadByPhone(phone);
        SecurityAppUser securityUser=null;
        if (Result.isSuccess(result)) {
            AppUserDto appUser = result.getData();
            if (null != appUser) {
                appUser.setClientId(RequestUtils.getOAuth2ClientId());
                securityUser = new SecurityAppUser(appUser);
                securityUser.setAuthenticationIdentity(AuthenticationIdentityEnum.MOBILE.getValue());   // 认证身份标识:mobile
            }else {
                throw new UsernameNotFoundException(MessageConstant.MOBILE_NOT_BOUND);
            }
        }
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
        return securityUser;
    }

    /**
     * @description openId认证方式
     * @param openId
     * @return
     */
    public UserDetails loadByOpenId(String openId)  {
        Result<AppUserDto> result = apiFeignClient.loadByOpenId(openId);
        SecurityAppUser securityUser=null;
        if (Result.isSuccess(result)) {
            AppUserDto appUser = result.getData();
            if (null != appUser) {
                appUser.setClientId(RequestUtils.getOAuth2ClientId());
                securityUser = new SecurityAppUser(appUser);
                securityUser.setAuthenticationIdentity(AuthenticationIdentityEnum.OPENID.getValue());   // 认证身份标识:openId
            }else {
                throw new UsernameNotFoundException(MessageConstant.WECHATLOGIN_EXIST);
            }
        }
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
        return securityUser;
    }
}
