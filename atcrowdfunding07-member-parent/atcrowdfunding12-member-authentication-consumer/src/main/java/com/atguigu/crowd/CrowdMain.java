package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author linzihao
 * @create 2022-10-17-19:48
 */

//启用Feign客户端功能
@EnableFeignClients
@SpringBootApplication
public class CrowdMain {

    public static void main(String[] args) {
        SpringApplication.run(CrowdMain.class,args);
    }

}
