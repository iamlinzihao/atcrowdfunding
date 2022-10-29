package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author linzihao
 * @create 2022-10-23-14:58
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class CrowdOrderClass {

    public static void main(String[] args) {
        SpringApplication.run(CrowdOrderClass.class,args);
    }

}