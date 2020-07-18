package com.jee.boot.common.utils;

import javafx.util.Pair;
import org.apache.commons.lang3.time.FastDateFormat;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 *
 * @author jeeLearner
 */
public class DateUtils {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    private static final String[] PATTERNS = {
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy-MM",  // 19, 16, 10, 7  日期：6-10  时间：5-8  日期时间：12-19  长度判断
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd", "yyyy/MM",
            "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM.dd", "yyyy.MM",
            "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMMdd", "yyyyMM" // 14, 12, 8, 6
    };

    private static final DateTimeFormatter DEFAULT_FORMATTER_DATETIME = DateTimeFormatter.ofPattern(PATTERNS[0]);
    private static final DateTimeFormatter DEFAULT_FORMATTER_DATE = DateTimeFormatter.ofPattern(PATTERNS[2]);
    private static final DateTimeFormatter DEFAULT_FORMATTER_TIME = DateTimeFormatter.ofPattern(HH_MM_SS);


    /**
     * format  datetime
     * @return
     */
    public static final String now() {
        return formatDateTime(LocalDateTime.now());
    }
    public static final String formatDateTime(final LocalDateTime dateTime) {
        return autoFormatDateTime(YYYY_MM_DD_HH_MM_SS, dateTime);
    }

    /**
     * format  date
     * @return
     */
    public static final String nowDate() {
        return formatDate(LocalDate.now());
    }
    public static final String formatDate(final LocalDate date) {
        return autoFormatDateTime(YYYY_MM_DD, date);
    }

    /**
     * format  time
     * @return
     */
    public static final String nowTime() {
        return formatTime(LocalTime.now());
    }
    public static final String formatTime(final LocalTime time) {
        return autoFormatDateTime(HH_MM_SS, time);
    }


    public static final String autoFormatDateTime(final String format, final Temporal temporal) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if (temporal instanceof LocalDateTime){
            return ((LocalDateTime) temporal).format(formatter);
        } else if(temporal instanceof LocalDate){
            return ((LocalDate) temporal).format(formatter);
        } else if (temporal instanceof LocalTime){
            return ((LocalTime) temporal).format(formatter);
        } else {
            return null;
        }
    }

    /**
     * 根据format判断DateTime类型
     *      在excelUtil自动解析中使用
     * @param format
     * @return
     */
//    public static Class<Temporal> getDateTimeByFormat(final String format) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
//        int length = format.length();
//        if (format.startsWith("yyyy")){
//            //带日期
//            if (format.contains("HH") || format.contains("hh")){
//                return LocalDateTime.class;
//            } else {
//                return LocalDate;
//            }
//        } else {
//            //时间
//            return LocalTime;
//    }

    /**
     * 获取LocalDateTime实例
     * @param format
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseDateTime(final String format, final String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateTime, formatter);
    }


    /**
     * 自动适配pattern
     *      这里用到了org.apache.commons.lang3.time.DateUtils
     * @param str
     * @return
     */
    public static LocalDateTime autoParseDateTime(Object str) {
        try {
            return format(autoParseDate(str));
        } catch (ParseException e) {
            return null;
        }
    }
    public static Date autoParseDate(Object str) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(str.toString(), PATTERNS);
    }

    public static void main(String[] args) {
        System.out.println(autoParseDateTime("2020-01-01"));
        System.out.println(autoParseDateTime("2020-01-01 12:12"));
    }


    /**
     * 增加时间
     * @param format
     * @param time
     * @param amount
     * @param unit
     * @return
     */
    public static final String plusDateTime(final String format,final LocalDateTime time, int amount, ChronoUnit unit) {
        LocalDateTime dateTime = time.plus((long) amount, unit);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath(){
        return autoFormatDateTime(PATTERNS[6], LocalDate.now());
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime(){
        return autoFormatDateTime(PATTERNS[14], LocalDate.now());
    }

    /**
     * 获取服务器启动时间
     */
    public static LocalDateTime getServerStartDate(){
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return getDateTime(time);
    }

    /**
     * 获取时间戳
     * @param dateTime
     * @return
     */
    public static long getTimestamp(LocalDateTime dateTime){
        return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 根据时间戳获取LocalDateTime
     * @param timestamp
     * @return
     */
    public static LocalDateTime getDateTime(long timestamp){
        return LocalDateTime.ofEpochSecond(timestamp/1000,0, ZoneOffset.ofHours(8));
    }

    /**
     * LocalDateTime ==> Date
     * @param dateTime
     * @return
     */
    public static Date parse(LocalDateTime dateTime){
        String s = formatDateTime(dateTime);
        try {
            return FastDateFormat.getInstance(YYYY_MM_DD_HH_MM_SS).parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Date ==> LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime format(Date date){
        String format = FastDateFormat.getInstance(YYYY_MM_DD_HH_MM_SS).format(date);
        return LocalDateTime.parse(format, DEFAULT_FORMATTER_DATETIME);
    }


    /**
     * 计算两个时间差
     */
    public static String getDateBetween(Temporal startInclusive, Temporal endExclusive){
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        Duration duration = Duration.between(startInclusive, endExclusive);
        long millis = duration.toMillis();
        // 计算差多少天
        long days = millis / nd;
        // 计算差多少小时
        long hours = millis % nd / nh;
        // 计算差多少分钟
        long minutes = millis % nd % nh / nm;
        // 计算差多少秒//输出结果
         long seconds = millis % nd % nh % nm / ns;
        return days+"天"+hours+"小时"+minutes+"分钟"+seconds+"秒";
    }

    /**
     * 将时间段按小时分割成多个时间段
     * @param stTime 开始时间
     * @param edTime 结束时间
     * @param duration 分隔小时数
     * @return
     */
    public static List<Pair<String, String>> splitTimesByHours(String stTime, String edTime, long duration) {
        List<Pair<String, String>> list = new ArrayList<>();
        LocalDateTime startTime = LocalDateTime.parse(stTime);
        LocalDateTime endTime = LocalDateTime.parse(edTime);
        LocalDateTime tempTime;
        while (startTime.isBefore(endTime)) {
            startTime = startTime.plusHours(duration);
            tempTime = startTime.plusSeconds(-1L);
            if (startTime.isAfter(endTime)){
                tempTime = endTime;
            }
            list.add(new Pair<String, String>(startTime.minusHours(duration).format(DEFAULT_FORMATTER_DATETIME), tempTime.format(DEFAULT_FORMATTER_DATETIME)));
        }
        return list;
    }

}

