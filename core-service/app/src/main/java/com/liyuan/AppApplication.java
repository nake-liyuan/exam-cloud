package com.liyuan;

import cn.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author liyuan
 * @date 2022/10/15
 * @project exam-cloud
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EsMapperScan("com.liyuan.mapper.ee")
@EnableTransactionManagement
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class,args);
    }
}
