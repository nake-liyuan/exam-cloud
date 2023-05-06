package com.liyuan.api;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author liyuan
 * @date 2022/10/19
 * @project exam-cloud
 */
@RetrofitClient(serviceId = "exam-auth")
public interface AuthClientFeignClient {

    @GET(value = "/oauth/getBCrypt")
    String getBCrypt(@Query("password") String password);
}
