package com.jee.boot.alone.license.client.utils;

import java.util.Properties;

public class YmlUtil {

    private static Properties ymlProperties = new Properties();

    public YmlUtil(Properties properties){
        ymlProperties = properties;
    }

    public static String getStrYmlVal(String key){
        return ymlProperties.getProperty(key);
    }

    public static Integer getIntegerYmlVal(String key){
        return Integer.valueOf(ymlProperties.getProperty(key));
    }

}