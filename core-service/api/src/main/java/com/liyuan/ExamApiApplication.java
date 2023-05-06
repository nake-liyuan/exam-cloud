package com.liyuan;

import cn.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liyuan
 * @date 2022/10/15
 * @project exam-cloud
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableCaching
@EsMapperScan("com.liyuan.mapper.ee")
public class ExamApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamApiApplication.class,args);
    }
}
