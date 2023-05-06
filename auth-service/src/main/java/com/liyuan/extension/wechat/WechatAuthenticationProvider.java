package com.liyuan.extension.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.liyuan.api.ApiFeignClient;
import com.liyuan.api.Result;
import com.liyuan.api.ResultCode;
import com.liyuan.entity.AppUser;
import com.liyuan.entity.AppUserDto;
import com.liyuan.userdetails.UserDetailsServiceImpl;
import lombok.Data;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.HashSet;

/**
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */
@Data
public class WechatAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;
    private WxMaService wxMaService;
    private ApiFeignClient apiFeignClient;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;
        String code = (String) authenticationToken.getPrincipal();
        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            throw new YAMLException("登录失败，请重新登录");
        }
        String openid = sessionInfo.getOpenid();
        Result<AppUserDto> appUserDtoResult = apiFeignClient.loadByOpenId(openid);
        if (appUserDtoResult!=null&& ResultCode.USER_NOT_EXIST.getCode().equals(appUserDtoResult.getCode())){
            String sessionKey = sessionInfo.getSessionKey();
            String encryptedData = authenticationToken.getEncryptedData();
            String iv = authenticationToken.getIv();
            // 解密 encryptedData 获取用户信息
            WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
            AppUser user = new AppUser();
            user.setNickName(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setAvatarUrl(userInfo.getAvatarUrl());
            user.setOpenId(openid);
            Result result = apiFeignClient.appUserRegister(user);
            if(result!=null&&ResultCode.SYSTEM_EXECUTION_ERROR.getCode().equals(result.getCode())){
                throw new YAMLException("注册失败");
            }
        }
        UserDetails userDetails = ((UserDetailsServiceImpl) userDetailsService).loadByOpenId(openid);
        WechatAuthenticationToken result = new WechatAuthenticationToken(userDetails, new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
