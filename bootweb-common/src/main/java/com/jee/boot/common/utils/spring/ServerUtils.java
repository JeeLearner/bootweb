package com.jee.boot.common.utils.spring;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeeLearner
 * @version V1.0
 */
public class ServerUtils {
    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     *
     * @return 服务地址
     */
    public static String getUrl() {
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }

    /**
     * http://localhost:8080/news/main/list.jsp
     *
     * 1. getServletPath():获取能够与“url-pattern”中匹配的路径，注意是完全匹配的部分，*的部分不包括。
     *      /main/list.jsp
     * 2. getPageInfo():与getServletPath()获取的路径互补，能够得到的是“url-pattern”中*d的路径部分
     * 3. getContextPath():获取项目的根路径
     *      /news
     * 4. getRequestURI:获取根路径到地址结尾
     *      /news/main/list.jsp
     * 5. getRequestURL:获取请求的地址链接（浏览器中输入的地址）
     * 6. getServletContext().getRealPath(“/”):获取“/”在机器中的实际地址
     * 7. getScheme():获取的是使用的协议(http 或https)
     * 8. getProtocol():获取的是协议的名称(HTTP/1.11)
     * 9. getServerName():获取的是域名(xxx.com)
     * 10. getLocalName:获取到的是IP
     */
}

