package com.jee.boot.shiro.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 确保应用退出时能关闭后台线程
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class ShutdownManager {
    private static final Logger logger = LoggerFactory.getLogger(ShutdownManager.class);


}

