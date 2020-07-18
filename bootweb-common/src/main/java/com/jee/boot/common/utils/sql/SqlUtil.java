package com.jee.boot.common.utils.sql;

import com.jee.boot.common.utils.text.JeeStringUtils;

/**
 * sql操作工具类
 *
 * @author jeeLearner
 * @version V1.0
 */
public class SqlUtil {
    /**
     * 仅支持字母、数字、下划线、空格、逗号（支持多个字段排序）
     */
    private static final String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (JeeStringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
            return JeeStringUtils.EMPTY;
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    private static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

}

