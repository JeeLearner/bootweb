package com.jee.boot.common.filter;

import com.jee.boot.common.utils.text.JeeStringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 防止XSS攻击的过滤器
 *
 * @author jeeLearner
 * @version V1.0
 */
public class XssFilter implements Filter {

    /**
     * 排除链接
     */
    public List<String> excludes = new ArrayList<>();

    /**
     * xss过滤开关
     */
    public boolean enabled = false;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String tempExcludes = filterConfig.getInitParameter("excludes");
        String tempEnabled = filterConfig.getInitParameter("enabled");
        if (JeeStringUtils.isNotEmpty(tempExcludes)){
            String[] url = tempExcludes.split(",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludes.add(url[i]);
            }
        }
        if (JeeStringUtils.isNotEmpty(tempEnabled)){
            enabled = Boolean.valueOf(tempEnabled);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeURL(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    @Override
    public void destroy(){ }

    /**
     * 以下情况不走xssFilter：
     *      enabled=false  未开启xssFilter
     *      excludes 排除链接 包含的请求路径
     * @param request
     * @param response
     * @return
     */
    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        if (!enabled) {
            return true;
        }
        if (JeeStringUtils.isNotEmpty(excludes)){
            String url = request.getServletPath();
            for (String pattern : excludes) {
                Pattern p = Pattern.compile("^" + pattern);
                Matcher m = p.matcher(url);
                if (m.find()){
                    return true;
                }
            }
        }
        return false;
    }
}

