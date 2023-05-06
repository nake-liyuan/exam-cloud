package com.liyuan.api;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.liyuan.entity.AppUser;
import com.liyuan.entity.AppUserDto;
import com.liyuan.entity.OauthClient;
import com.liyuan.entity.UserDto;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @description 定义Http接口，用于调用远程的service-api服务
 * @author liyuan
 * @date 2022/11/21
 * @project exam-cloud
 */

@RetrofitClient(serviceId = "service-api")
public interface ApiFeignClient {

    /**
     * @description 按clientId获取OAuth2客户端
     * @param clientId
     */
    @GET("/oauth-clients/getOAuth2ClientById")
    Result<OauthClient> getOAuth2ClientById(@Query("clientId") String clientId);

    /**
     * @description 根据用户名获取通用用户信息
     * @param username
     */
    @GET("/user/loadByUsername")
    Result<UserDto> loadUserByUsername(@Query("username") String username);

    /**
     * @description 根据手机号码获取通用用户信息
     * @param phone
     */
    @GET("/user/loadByPhone")
    Result<AppUserDto> loadByPhone(@Query("phone") String phone);

    /**
     * @description 根据openId获取通用用户信息
     * @param openId
     */
    @GET("/user/loadByOpenId")
    Result<AppUserDto> loadByOpenId(@Query("openId") String openId);

    /**
     * @description 根据wechat信息注册小程序用户
     * @param appUser
     */
    @POST("/user/appUserRegister")
    Result appUserRegister(@Body AppUser appUser);
}
