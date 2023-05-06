package com.liyuan.loginlog;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.liyuan.constant.AuthConstant;
import com.liyuan.util.ip.IpInfoUtil;
import com.liyuan.util.ip.IpToAddressUtil;
import com.liyuan.utils.RequestUtils;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author liyuan
 * @date 2022/10/23
 * @project exam-cloud
 */
@Aspect
@Component
@Slf4j
public class LoginLogAspect {
    @Pointcut("execution(public * com.liyuan.controller.AuthController.postAccessToken(..))")
    public void Log() {
    }

    @Around("Log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime startTime = LocalDateTime.now();
        Object result = joinPoint.proceed();
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 刷新token不记录
        String grantType=request.getParameter(AuthConstant.GRANT_TYPE_KEY);
        if(grantType.equals(AuthConstant.REFRESH_TOKEN)){
            return result;
        }
        // 时间统计
        LocalDateTime endTime = LocalDateTime.now();
        long elapsedTime = Duration.between(startTime, endTime).toMillis(); // 请求耗时（毫秒）

        // 获取接口描述信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String description = signature.getMethod().getAnnotation(ApiOperation.class).value();// 方法描述

        String date = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // 索引名需要，因为默认生成索引的date时区不一致

        // 获取token
        String token = Strings.EMPTY;
        String clientId = RequestUtils.getOAuth2ClientId();
        String jti = Strings.EMPTY;
        String username = Strings.EMPTY;
        if (request != null) {
            JSONObject jsonObject = JSONUtil.parseObj(result);
            token=JSONUtil.parseObj(jsonObject.getStr("data")).getStr("token");
             jti = JWTUtil.parseToken(token).getPayload("jti").toString();
            username = JWTUtil.parseToken(token).getPayload("user_name").toString();
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
        String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
        String os = userAgent.getOperatingSystem().getName();
        String browser = userAgent.getBrowser().toString();

        String clientIP = IpInfoUtil.getIpAddr(request);
        String region = "127.0.0.1".equals(clientIP)?"本地":IpToAddressUtil.getCityInfo(clientIP);
        MDC.put("elapsedTime", StrUtil.toString(elapsedTime));
        MDC.put("description", description);
        MDC.put("region", region);
        MDC.put("username", username);
        MDC.put("token", token);
        MDC.put("clientIP", clientIP);
        MDC.put("clientID", clientId);
        MDC.put("os", os);
        MDC.put("browser", browser);
        MDC.put("clientType", clientType);
        MDC.put("method", request.getMethod());
        MDC.put("uri", request.getRequestURI());
        MDC.put("url", request.getRequestURL().toString());
        MDC.put("date", date);

        log.info("{} 登录，耗费时间 {} 毫秒", username, elapsedTime);
        return result;
    }

}