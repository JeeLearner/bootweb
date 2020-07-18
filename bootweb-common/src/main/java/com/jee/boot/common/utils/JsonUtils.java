package com.jee.boot.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * JSON工具类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class JsonUtils {

    /**
     * object => string
     * @param obj
     * @return
     */
    public static String toString(Object obj){
        return JSON.toJSONString(obj);
    }

    public static JSONObject toJSONObj(String str){
        return JSON.parseObject(str);
    }
}

