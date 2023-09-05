package com.sangeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.sangeng.mapper")
@EnableScheduling   //使用@EnableScheduling注解开启定时任务功能我们可以在配置类上加上@EnableScheduling
public class SanGengBlogController {
    public static void main(String[] args) {
        SpringApplication.run(SanGengBlogController.class,args);
    }
}
