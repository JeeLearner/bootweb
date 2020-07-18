package com.jee.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序入口
 *
 * @author jeeLearner
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.jee.boot.gen.mapper"})
public class GenApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenApplication.class, args);
    }
}

