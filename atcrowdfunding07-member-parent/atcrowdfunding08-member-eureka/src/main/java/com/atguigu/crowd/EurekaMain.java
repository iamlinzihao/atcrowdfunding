package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author linzihao
 * @create 2022-10-17-19:48
 */

@EnableEurekaServer
@SpringBootApplication
public class EurekaMain {

    public static void main(String[] args) {
        SpringApplication.run(EurekaMain.class,args);
    }

}
