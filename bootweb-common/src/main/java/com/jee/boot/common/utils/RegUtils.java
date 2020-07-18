package com.jee.boot.common.utils;

import javafx.util.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jeeLearner
 * @version V1.0
 */
public class RegUtils {

    /**
     * 用户名格式限制
     */
    public static final String USERNAME_PATTERN = "^[a-z][a-zA-Z0-9_\\.]*$";

    /**
     * 手机号码格式限制
     */
    public static final String MOBILE_PHONE_NUMBER_PATTERN = "^0{0,1}(13[0-9]|15[0-9]|14[0-9]|18[0-9])[0-9]{8}$";

    /**
     * 邮箱格式限制
     */
    public static final String EMAIL_PATTERN = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?";

    /**
     * 中文格式
     */
    public static final String CHINESE_PATTERN = "[\\u4e00-\\u9fa5]+";

    /**
     * 文件下载  文件名格式
     *      例：用户信息_20200624010102234.xlsx
     */
    public static final String FILENAME_PATTERN = "[\\u4e00-\\u9fa5]+_[0-9]{13}\\.(xlsx|xls)";

    /**
     * 文件下载  文件名格式
     *      例：用户信息_cbe5048c-2595-4897-b78a-a09ffda600da.xlsx
     */
    public static final String FILENAME_PATTERN_UUID = "[\\u4e00-\\u9fa5]+_[a-zA-Z0-9\\-]+\\.(xlsx|xls)";


    /**
     * 注释格式 <!-- xxx -->
     */
    private static final Pattern P_COMMENTS = Pattern.compile("<!--(.*?)-->", Pattern.DOTALL);

    /**
     * https://deerchao.cn/tutorials/regex/regex.htm
     *
     * @param args
     */
    public static void main(String[] args) {
        String s = "<!-- 测试 -->";
        final Matcher m = P_COMMENTS.matcher(s);
        System.out.println("xml文件注释匹配：" + m.find());
        System.out.println(m.group(1));  // (.*?)   测试   （注意：有空格）
        System.out.println(m.group(0));  // <!-- 测试 -->

        String fileName = "用户信息_1593221870170.xlsx";
        boolean matches = fileName.matches(FILENAME_PATTERN);
        System.out.println("excel文件名匹配：" + matches);
        String realFileName = fileName.substring(0, fileName.indexOf("_")+1) + System.currentTimeMillis();
        System.out.println(realFileName);

        //用户信息_cbe5048c-2595-4897-b78a-a09ffda600da.xlsx
        String str = "用户信息_cbe5048c-2595-4897-b78a-a09ffda600da.xlsx";
        boolean mat = str.matches(FILENAME_PATTERN_UUID);
        System.out.println("excel文件名uuid匹配：" + mat);
        String realFileName2 = str.substring(0, str.indexOf("_")+1) + System.currentTimeMillis() + str.substring(str.lastIndexOf("."));
        System.out.println(realFileName2);

    }
}