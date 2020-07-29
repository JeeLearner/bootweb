package com.jee.boot.framework.interceptor;

import com.jee.boot.common.utils.JsonUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 判断请求url和数据是否和上一次相同，
 *  如果和上次相同，则是重复提交表单。 有效时间为5秒内。
 *
 * @author jeeLearner
 * @version V1.0
 */
@Component
public class SameUrlDataInterceptor extends AbstractRepeatSubmitInterceptor {

    public final String REPEAT_KEY = "repeatKey";
    public final String REPEAT_PARAMS = "repeatParams";
    public final String REPEAT_TIME = "repeatTime";

    /**
     * 间隔时间，单位:秒 默认10秒
     *
     * 两次相同参数的请求，如果间隔时间大于该参数，系统不会认定为重复提交的数据
     */
    private int intervalTime = 10;
    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    /**
     * 构建拦截规则
     *      true : 拦截
     *      false: 放行
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request) throws Exception {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String params = JsonUtils.toString(request.getParameterMap());
        dataMap.put(REPEAT_PARAMS, params);
        dataMap.put(REPEAT_TIME, LocalDateTime.now());

        // 请求地址（作为存放session的key值）
        String url = request.getRequestURI();
        Map<String, Object> sessionMap = new HashMap<String, Object>();

        HttpSession session = request.getSession();
        Object sessionObj = session.getAttribute(REPEAT_KEY);
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
        session.setAttribute(REPEAT_KEY, sessionMap);

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
        LocalDateTime oldTime = (LocalDateTime) oldDataMap.get(REPEAT_TIME);
        return oldTime.plusSeconds(this.intervalTime).isAfter(time) ? true : false;
    }
}

