package top.zy68.personservice.utils;

import org.apache.logging.log4j.util.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @ClassName DateUtil
 * @Description 日期转换工具类
 * @Author Sustart
 * @Date2022/2/22 21:14
 * @Version 1.0
 **/
public class DateUtil {
    public static final String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_CN = "yyyy年MM月dd日";

    /**
     * 字符串转日期
     * 必须同时传入日期字符串和转换格式字符串.
     *
     * @param dateStr 日期的字符串形式
     * @param pattern 格式
     * @return 返回 Date 类型日期或 null
     */
    public static Date stringToDate(String dateStr, String pattern) {
        if (Strings.isBlank(dateStr) || Objects.isNull(pattern)) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;

        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 字符串转日期
     * 必须同时传入日期和转换格式字符串.
     *
     * @param date    日期的字符串形式
     * @param pattern 格式
     * @return 返回 String 类型日期
     */
    public static String dateToString(Date date, String pattern) {
        if (Objects.isNull(date) || Objects.isNull(pattern)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}
