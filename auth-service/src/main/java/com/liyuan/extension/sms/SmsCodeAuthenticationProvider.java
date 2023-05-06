package com.liyuan.extension.sms;

import cn.hutool.core.util.StrUtil;
import com.liyuan.constant.AuthConstant;
import com.liyuan.exceptionhandler.YKException;
import com.liyuan.userdetails.UserDetailsServiceImpl;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;

/**
 * @author liyuan
 * @date 2022/11/20
 * @project exam-cloud
 */
@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;
    private StringRedisTemplate redisTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String code = (String) authenticationToken.getCredentials();
            String codeKey = AuthConstant.SMS_CODE_PREFIX + mobile;
            String correctCode = redisTemplate.opsForValue().get(codeKey);
            // 验证码比对
            if (StrUtil.isBlank(correctCode) || !code.equals(correctCode)) {
                throw new YKException("A0131","短信校验码输入错误");
            }
            // 比对成功删除缓存的验证码
//            redisTemplate.delete(codeKey);
        UserDetails userDetails = ((UserDetailsServiceImpl)userDetailsService).loadUserByPhone(mobile);
        SmsCodeAuthenticationToken result = new SmsCodeAuthenticationToken(userDetails, authentication.getCredentials(), new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
