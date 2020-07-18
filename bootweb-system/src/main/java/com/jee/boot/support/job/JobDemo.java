package com.jee.boot.support.job;

import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author jeeLearner
 * @version V1.0
 */
@Component("jobDemo")
public class JobDemo {

    public void noParam(){
        System.out.println("jobDemo ==> 执行无参方法......" + LocalDateTime.now());
    }

    public void singleParam(String param){
        System.out.println("jobDemo ==> 执行单参方法("+ param +")......" + LocalDateTime.now());
    }

    public void multipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(JeeStringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }
}

