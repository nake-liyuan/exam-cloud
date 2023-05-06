package com.liyuan.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
@Data
public class SecurityAppUser implements UserDetails {
    /**
     * ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户状态
     */
    private Boolean enabled;

    /**
     * 登录客户端ID
     */
    private String clientId;

    /**
    * 认证身份标识，枚举值如下：AuthenticationIdentityEnum
    */
    private String authenticationIdentity;


    public SecurityAppUser(AppUserDto appUserDto) {
        this.setId(appUserDto.getId());
        this.setUsername(appUserDto.getUsername());
        this.setEnabled(appUserDto.getEnabled() == 1);
        this.setClientId(appUserDto.getClientId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
