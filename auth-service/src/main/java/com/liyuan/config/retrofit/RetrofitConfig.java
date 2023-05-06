package com.liyuan.config.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.core.ServiceInstanceChooser;
import com.github.lianjiatech.retrofit.spring.boot.core.SpringCloudServiceInstanceChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liyuan
 * @date 2022/11/9
 * @project exam-cloud
 */
@Configuration
public class RetrofitConfig {

    @Bean
    @Autowired
    public ServiceInstanceChooser serviceInstanceChooser(LoadBalancerClient loadBalancerClient) {
        return new SpringCloudServiceInstanceChooser(loadBalancerClient);
    }


}
