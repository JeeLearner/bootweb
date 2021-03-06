package com.jee.boot.common.utils.http;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 通用http工具封装
 *
 * @author jeeLearner
 * @version V1.0
 */
public class HttpHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    /**
     * 通过request获取请求内容
     * @param request
     * @return
     */
    public static String getBodyString(ServletRequest request) {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        try(InputStream inputStream = request.getInputStream()) {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e){
            LOGGER.warn("getBodyString出现问题！");
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //LOGGER.error(org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e));
                    LOGGER.error(ExceptionUtils.getStackTrace(e));
                }
            }
        }
        return sb.toString();
    }

}

