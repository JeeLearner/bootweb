package com.jee.boot.common.core.web;

import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.DateUtils;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * web层通用数据处理
 *
 * @author jeeLearner
 */
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     *
     * @InitBinder:
     *     作用：给Binder做初始化的，被此注解的方法可以对WebDataBinder初始化。webDataBinder是用于表单到方法的数据绑定的！
     *     注意：只在@Controller中注解方法来为这个控制器注册一个绑定器初始化方法，方法只对本控制器有效。
     */
    @InitBinder
    public void initBinder(WebDataBinder binder){
        // Date 类型转换
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.autoParseDateTime(text));
            }
        });
    }

    /**
     * info结果封装
     *
     * @param t
     * @param <T>
     * @return
     */
    public <T> R info(T t) {
        return R.ok().data("info", t);
    }

    /**
     * 新增/修改 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected R res(int rows){
        return rows > 0 ? R.ok() : R.error().msg("操作失败");
    }


    /**
     * 获取request
     */
    public HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return JeeStringUtils.format("redirect:{}", url);
    }
}

