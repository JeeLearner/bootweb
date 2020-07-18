package com.jee.boot.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jeeLearner
 * @version V1.0
 */
@Component
@ConfigurationProperties(prefix = "jee")
public class JeeProperties {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

