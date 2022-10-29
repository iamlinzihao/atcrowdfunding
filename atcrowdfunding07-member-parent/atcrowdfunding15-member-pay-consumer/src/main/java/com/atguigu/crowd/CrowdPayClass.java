package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author linzihao
 * @create 2022-10-27-12:25
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class CrowdPayClass {
    public static void main(String[] args) {
        SpringApplication.run(CrowdPayClass.class,args);
    }
}
