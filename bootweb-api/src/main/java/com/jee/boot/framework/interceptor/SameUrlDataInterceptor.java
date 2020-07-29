package com.jee.boot.framework.interceptor;

import com.jee.boot.api.redis.RedisCache;
import com.jee.boot.common.filter.RepeatedlyRequestWrapper;
import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.common.utils.JsonUtils;
import com.jee.boot.common.utils.http.HttpHelper;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 判断请求url和数据是否和上一次相同，
 *  如果和上次相同，则是重复提交表单。 有效时间为10秒内。
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class SameUrlDataInterceptor extends AbstractRepeatSubmitInterceptor {

    public final String REPEAT_KEY = "repeatKey";
    public final String REPEAT_PARAMS = "repeatParams";
    public final String REPEAT_TIME = "repeatTime";

    @Autowired
    RedisCache redisCache;

    /**
     * 间隔时间，单位:秒 默认10秒
     *
     * 两次相同参数的请求，如果间隔时间大于该参数，系统不会认定为重复提交的数据
     */
    private int intervalTime = 10*60;
    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    /**
     * 构建拦截规则
     *      true : 拦截
     *      false: 放行
     *      获取参数：==>参考：https://www.cnblogs.com/zjrodger/p/6973333.html
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request) throws Exception {
        //String params = JsonUtils.toString(request.getParameterMap());
        //注意：必须使用RepeatableFilter将request转换，否则报异常：
        //java.lang.ClassCastException: org.springframework.security.web.servletapi.HttpServlet3RequestFactory$Servlet3SecurityContextHolderAwareRequestWrapper cannot be cast to com.jee.boot.common.filter.RepeatedlyRequestWrapper
        RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
        String nowParams = HttpHelper.getBodyString(repeatedlyRequest);

        // body参数为空，获取Parameter的数据
        if (JeeStringUtils.isEmpty(nowParams)){
            nowParams = JsonUtils.toString(request.getParameterMap());
        }

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(REPEAT_PARAMS, nowParams);
        dataMap.put(REPEAT_TIME, LocalDateTime.now());

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();
        Map<String, Object> sessionMap = new HashMap<String, Object>();

        Object sessionObj = redisCache.getCacheObject(REPEAT_KEY);
        if (sessionObj != null){
            sessionMap = (Map<String, Object>) sessionObj;
            if (sessionMap.containsKey(url)){
                Map<String, Object> oldDataMap = (Map<String, Object>) sessionMap.get(url);
                if (compareParams(dataMap, oldDataMap) && compareTime(dataMap, oldDataMap)) {
                    return true;
                }
            }
        }
        sessionMap.put(url, dataMap);
        redisCache.setCacheObject(REPEAT_KEY, sessionMap, intervalTime, TimeUnit.SECONDS);
        return false;
    }


    /**
     * 判断参数是否相同
     * @param dataMap
     * @param oldDataMap
     * @return
     */
    private boolean compareParams(Map<String, Object> dataMap, Map<String, Object> oldDataMap) {
        if (oldDataMap == null){
            if (dataMap ==null){
                return true;
            } else {
                return false;
            }
        } else {
            String params = (String) dataMap.get(REPEAT_PARAMS);
            String oldParams = (String) oldDataMap.get(REPEAT_PARAMS);
            return params.equals(oldParams);
        }
    }

    /**
     * 判断两次间隔时间
     * @param dataMap
     * @param oldDataMap
     * @return
     */
    private boolean compareTime(Map<String, Object> dataMap, Map<String, Object> oldDataMap) {
        LocalDateTime time = (LocalDateTime) dataMap.get(REPEAT_TIME);
        //oldDataMap.get(REPEAT_TIME) 这里redis存储时会自动转为时间戳
        long cacheTime = (Long) oldDataMap.get(REPEAT_TIME);
        LocalDateTime oldTime = DateUtils.getDateTime(cacheTime);
        return oldTime.plusSeconds(this.intervalTime).isAfter(time) ? true : false;
    }
}

