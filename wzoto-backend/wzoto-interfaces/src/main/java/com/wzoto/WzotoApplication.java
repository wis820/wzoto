package com.wzoto;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * 学霸到家 - 线下1对1家教撮合平台 启动类
 */
@SpringBootApplication
@MapperScan("com.wzoto.infrastructure.mapper")
@EnableRetry
public class WzotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WzotoApplication.class, args);
    }
}