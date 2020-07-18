package com.jee.boot.common.core.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jee.boot.common.core.result.R;
import com.jee.boot.common.utils.spring.ServletUtils;
import com.jee.boot.common.utils.text.JeeStringUtils;
import com.jee.boot.common.utils.sql.SqlUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jeeLearner
 * @version V1.0
 */
public class PageUtil<T> {
    /** 当前记录起始索引 的属性名*/
    private static final String PAGE_NUM = "pageNum";
    /** 每页显示记录数 的属性名 */
    private static final String PAGE_SIZE = "pageSize";
    /** 排序列 */
    private static final String ORDER_BY_COLUMN = "orderByColumn";
    /** 排序的方向 "desc" 或者 "asc". */
    private static final String IS_ASC = "isAsc";
    private static final String PAGE_TOTAL = "total";
    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    /** 排序列 */
    private String orderByColumn;
    /** 排序的方向 "desc" 或者 "asc". */
    private String isAsc;

    /**
     * 分页主方法
     *
     * @param cacheLoadble
     * @param <T>
     * @return
     */
    public static <T> R page(CacheLoadble<T> cacheLoadble){
        return page(ServletUtils.getRequest(), cacheLoadble);
    }

    /**
     * 分页主方法
     * @param request
     * @param cacheLoadble
     * @param <T>
     * @return
     */
    public static <T> R page(HttpServletRequest request, CacheLoadble<T> cacheLoadble){
        Page<T> page = PageHelper.startPage(getPageNum(request), getPageSize(request), SqlUtil.escapeOrderBySql(getOrderBy(request)));
        T load = cacheLoadble.load();
        return R.ok().data(PAGE_TOTAL, page.getTotal()).data("list", load);
    }

    private static String getOrderBy(HttpServletRequest request){
        String orderByColumn = request.getParameter(ORDER_BY_COLUMN);
        String isAsc = request.getParameter(IS_ASC);
        if (JeeStringUtils.isEmpty(orderByColumn)){
            return "";
        }
        return JeeStringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    private static int getPageNum(HttpServletRequest request){
        String pageNumStr = request.getParameter(PAGE_NUM);
        int pageNum;
        if(JeeStringUtils.isEmpty(pageNumStr)){
            pageNum = 1;
        }else{
            pageNum = Integer.parseInt(pageNumStr);
        }
        return pageNum;
    }

    private static int getPageSize(HttpServletRequest request){
        String pageSizeStr = request.getParameter(PAGE_SIZE);
        int pageSize;
        if(JeeStringUtils.isEmpty(pageSizeStr)){
            pageSize = 1;
        }else{
            pageSize = Integer.parseInt(pageSizeStr);
        }
        return pageSize;
    }
}

