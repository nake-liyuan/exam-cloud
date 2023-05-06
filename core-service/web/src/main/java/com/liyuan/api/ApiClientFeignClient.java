package com.liyuan.api;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.POST;

/**
 * @author liyuan
 * @date 2023/3/10
 * @project exam-cloud
 */
@RetrofitClient(serviceId = "service-api")
public interface ApiClientFeignClient {

    @POST(value = "/permRolesRules/refreshPermRolesRules")
    Result refreshPermRolesRules();
}
