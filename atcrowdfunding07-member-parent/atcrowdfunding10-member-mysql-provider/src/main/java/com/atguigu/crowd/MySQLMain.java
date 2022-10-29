package com.atguigu.crowd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author linzihao
 * @create 2022-10-17-19:48
 */

@MapperScan("com.atguigu.crowd.mapper")
@SpringBootApplication
public class MySQLMain {

    public static void main(String[] args) {
        SpringApplication.run(MySQLMain.class,args);
    }

}
