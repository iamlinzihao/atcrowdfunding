package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author linzihao
 * @create 2022-10-17-19:48
 */

@EnableZuulProxy
@SpringBootApplication
public class ZuulMain {

    public static void main(String[] args) {
        SpringApplication.run(ZuulMain.class,args);
    }

}
