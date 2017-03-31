package com.michealyang.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by michealyang on 17/3/29.
 */
public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public final static String DefaultDayFormat = "yyyy-MM-dd";
    public final static String DefaultHourFormat = "yyyy-MM-dd HH";
    public final static String DefaultMinuteFormat = "yyyy-MM-dd HH:mm";
    public final static String DefaultSecondFormat = "yyyy-MM-dd HH:mm:ss";


    private final static String DATE_DAY_PATTERN = "\\d{4}-\\d{2}-\\d{2}";    //精确到天的格式
    private final static String DATE_HOUR_PATTERN = "\\d{4}-\\d{2}-\\d{2} \\d{2}";    //精确到天的格式
    private final static String DATE_MIN_PATTERN = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";    //精确到天的格式
    private final static String DATE_SEC_PATTERN = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";    //精确到秒的格式
    private final static Pattern pDay;
    private final static Pattern pHour;
    private final static Pattern pMin;
    private final static Pattern pSec;
    static {
        pDay = Pattern.compile(DATE_DAY_PATTERN);
        pHour = Pattern.compile(DATE_HOUR_PATTERN);
        pMin = Pattern.compile(DATE_MIN_PATTERN);
        pSec = Pattern.compile(DATE_SEC_PATTERN);
    }


    //返回当前的Unix时间戳
    public static long now(){
        return new Date().getTime() / 1000;
    }

    //返回当前时间的日期，格式为yyyy-MM-dd
    public static String today(){
        SimpleDateFormat formatter = new SimpleDateFormat(DefaultDayFormat);
        return formatter.format(new Date());
    }

    //返回当前时间的小时，格式为yyyy-MM-dd HH
    public static String currentHour(){
        SimpleDateFormat formatter = new SimpleDateFormat(DefaultHourFormat);
        return formatter.format(new Date());
    }

    //返回当前时间的小时，格式为yyyy-MM-dd HH:mm
    public static String currentMinute() {
        SimpleDateFormat formatter = new SimpleDateFormat(DefaultMinuteFormat);
        return formatter.format(new Date());
    }

    //返回当前时间的小时，格式为yyyy-MM-dd HH:mm:ss
    public static String currentSecond() {
        SimpleDateFormat formatter = new SimpleDateFormat(DefaultSecondFormat);
        return formatter.format(new Date());
    }

    //将yyyy-MM-dd格式的时间转换成unix时间戳
    public static long Day2UT(String day){
        Preconditions.checkArgument(StringUtils.isNotBlank(day));
        Preconditions.checkArgument(pDay.matcher(day).matches(), "时间格式不正确");
        SimpleDateFormat formatter = new SimpleDateFormat(DefaultDayFormat);
        try {
            return formatter.parse(day).getTime() / 1000;
        } catch (ParseException e) {
            logger.error("[Day2UT] Exception=#{}", e);
        }
        return 0l;
    }

    //将yyyy-MM-dd HH:mm:ss格式的时间转换成unix时间戳
    public static long Second2UT(String second){
        Preconditions.checkArgument(StringUtils.isNotBlank(second));
        Preconditions.checkArgument(pSec.matcher(second).matches(), "时间格式不正确");
        SimpleDateFormat formatter = new SimpleDateFormat(DefaultSecondFormat);
        try {
            return formatter.parse(second).getTime() / 1000;
        } catch (ParseException e) {
            logger.error("[Second2UT] Exception=#{}", e);
        }
        return 0l;
    }

    //Unix Timestamp转换成天，格式为yyyy-MM-dd
    public static String UT2Day(long seconds){
        SimpleDateFormat formatter = new SimpleDateFormat(DefaultDayFormat);
        return formatter.format(new Date(seconds * 1000));
    }

    //Unix Timestamp转换成秒，格式为yyyy-MM-dd HH:mm:ss
    public static String UT2Second(long seconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(DefaultSecondFormat);
        return formatter.format(new Date(seconds * 1000));
    }

    /**
     * 在时间day的基础上，往后hours个小时
     * @param day
     * @param hours
     * @return
     */
    public static String addHours(String day, int hours) {
        Preconditions.checkArgument(StringUtils.isNotBlank(day));
        Preconditions.checkArgument(hours >= 0, "时间需是正值");
        long a;
        if(pDay.matcher(day).matches()) {
            a = Day2UT(day);
        }else if(pSec.matcher(day).matches()) {
            a = Second2UT(day);
        }else {
            throw new IllegalArgumentException("不支持的时间格式");
        }
        long b = a + hours * 3600;
        return UT2Day(b);
    }

    /**
     * 在时间day的基础上，往前hours个小时
     * @param day
     * @param hours
     * @return
     */
    public static String minusHours(String day, int hours){
        Preconditions.checkArgument(StringUtils.isNotBlank(day));
        Preconditions.checkArgument(hours >= 0, "时间需是正值");
        long a;
        if(pDay.matcher(day).matches()) {
            a = Day2UT(day);
        }else if(pSec.matcher(day).matches()) {
            a = Second2UT(day);
        }else {
            throw new IllegalArgumentException("不支持的时间格式");
        }
        long b = a - hours * 3600;
        return UT2Day(b);
    }

    /**
     * 在day的基础上，往后days天
     * @param day
     * @param days
     * @return
     */
    public static String addDays(String day, int days){
        Preconditions.checkArgument(StringUtils.isNotBlank(day));
        Preconditions.checkArgument(days >= 0, "时间需是正值");
        long a;
        if(pDay.matcher(day).matches()) {
            a = Day2UT(day);
        }else if(pSec.matcher(day).matches()) {
            a = Second2UT(day);
        }else {
            throw new IllegalArgumentException("不支持的时间格式");
        }
        long b = a + days * 24 * 3600;
        return UT2Day(b);
    }

    /**
     * 在day的基础上，往前days天
     * @param day
     * @param days
     * @return
     */
    public static String minusDays(String day, int days){
        Preconditions.checkArgument(StringUtils.isNotBlank(day));
        Preconditions.checkArgument(days >= 0, "时间需是正值");
        long a;
        if(pDay.matcher(day).matches()) {
            a = Day2UT(day);
        }else if(pSec.matcher(day).matches()) {
            a = Second2UT(day);
        }else {
            throw new IllegalArgumentException("不支持的时间格式");
        }
        long b = a - days * 24 * 3600;
        return UT2Day(b);
    }

    /**
     * 返回day - days 到day的每一天的时间序列
     * <p>example:
     * day=2017-03-30, days=29
     * 返回
     * [2017-03-01, 2017-03-02, 2017-03-03, 2017-03-04, 2017-03-05, 2017-03-06, 2017-03-07, 2017-03-08, 2017-03-09, 2017-03-10,
     * 2017-03-11, 2017-03-12, 2017-03-13, 2017-03-14, 2017-03-15, 2017-03-16, 2017-03-17, 2017-03-18, 2017-03-19, 2017-03-20,
     * 2017-03-21, 2017-03-22, 2017-03-23, 2017-03-24, 2017-03-25, 2017-03-26, 2017-03-27, 2017-03-28, 2017-03-29, 2017-03-30]
     * </p>
     * @param day
     * @param days
     * @return
     */
    public static List<String> minusDaysArray(String day, int days) {
        Preconditions.checkArgument(StringUtils.isNotBlank(day));
        Preconditions.checkArgument(days >= 0, "时间需是正值");
        List<String> daysArray = Lists.newArrayListWithExpectedSize(days);
        long a;
        if(pDay.matcher(day).matches()) {
            a = Day2UT(day);
        }else if(pSec.matcher(day).matches()) {
            a = Second2UT(day);
        }else {
            throw new IllegalArgumentException("不支持的时间格式");
        }

        SimpleDateFormat formatter = new SimpleDateFormat(DefaultDayFormat);
        for(int i=days; i>=0; i--){
            long b = a - i * 24 * 3600;
            daysArray.add(formatter.format(new Date(b * 1000)));
        }
        return daysArray;
    }
}
