package com.liyuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liyuan
 * @date 2022/9/10
 * @project exam
 */
@EnableDiscoveryClient //nacos注册
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.liyuan")
@EnableCaching
public class ExamAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamAuthApplication.class,args);
    }
}
