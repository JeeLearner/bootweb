package com.jee.boot.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序入口
 *
 * @author jeeLearner
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.jee.boot.system.mapper"})
public class SysApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysApplication.class, args);
    }
}

