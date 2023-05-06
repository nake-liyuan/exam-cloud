package com.liyuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liyuan
 * @date 2022/9/10
 * @project exam
 */
@EnableDiscoveryClient //nacos注册
@SpringBootApplication
public class ExamGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamGatewayApplication.class,args);
    }
}
