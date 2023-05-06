package com.liyuan.api;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.liyuan.entity.applet.Message;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author liyuan
 * @date 2023/2/8
 * @project exam-cloud
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com/")
public interface AppletClient {

    @GET("cgi-bin/token")
    String getPerson(@Query("grant_type") String grantType,
                          @Query("appid") String appid,
                          @Query("secret") String secret);
    @POST("cgi-bin/message/subscribe/send")
    String sendTemplateMessage(@Query("access_token") String accessToken, @Body Message message);
}
