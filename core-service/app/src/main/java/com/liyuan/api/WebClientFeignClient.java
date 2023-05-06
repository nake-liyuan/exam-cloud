package com.liyuan.api;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.GET;

/**
 * @author liyuan
 * @date 2022/10/30
 * @project exam-cloud
 */
@RetrofitClient(serviceId = "service-web")
public interface WebClientFeignClient {

    @GET(value = "/subject/catalogue")
    Result subjectOption();
}
