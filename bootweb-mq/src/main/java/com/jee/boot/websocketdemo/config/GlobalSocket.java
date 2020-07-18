package com.jee.boot.websocketdemo.config;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jeeLearner
 * @version V1.0
 */

public class GlobalSocket {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static final AtomicInteger count = new AtomicInteger(0);
    private static volatile int onlineCount = 0;

    public static int getCount(){
        return count.get();
    }

    public static int addAndGetCount(int delta){
        return count.addAndGet(delta);
    }

    public static int minusAndGetCount(int delta){
        int num = count.addAndGet(-delta);
        return num < 0 ? 0 : num;
    }

    public static void main(String[] args) {
        System.out.println(count);
        minusAndGetCount(1);
        System.out.println(count);
    }
}

