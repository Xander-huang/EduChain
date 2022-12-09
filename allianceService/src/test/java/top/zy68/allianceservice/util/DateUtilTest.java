package top.zy68.allianceservice.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @ClassName DateUtilTest
 * @Description 日期工具类测试
 * @Author Sustart
 * @Date2022/2/22 21:47
 * @Version 1.0
 **/
// @SpringBootTest
public class DateUtilTest {
    /**
     * 测试字符串转日期
     *
     * @throws ParseException 转换异常
     */
    @Test
    void stringToDate() throws ParseException {
        Date date = new Date();

        // 测试 年月日格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 转换测试示例
        String dateStr = dateFormat.format(date);
        // 预期转换结果
        Date dateShort = dateFormat.parse(dateStr);
        Assert.isTrue(Objects.equals(DateUtil.stringToDate(dateStr, DateUtil.PATTERN_DATE), dateShort), "年月日格式日期不匹配");

        // 测试 年月日时间格式
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 转换测试示例
        String timeStr = timeFormat.format(date);
        // 预期转换结果
        Date timeShort = timeFormat.parse(timeStr);
        Assert.isTrue(Objects.equals(DateUtil.stringToDate(timeStr, DateUtil.PATTERN_TIME), timeShort), "年月日时分秒格式日期不匹配");

        // 测试 带中文的年月日格式
        SimpleDateFormat CNFormat = new SimpleDateFormat("yyyy年MM月dd日");
        // 转换测试示例
        String CNStr = CNFormat.format(date);
        // 预期转换结果
        Date CNShort = CNFormat.parse(CNStr);
        Assert.isTrue(Objects.equals(DateUtil.stringToDate(CNStr, DateUtil.PATTERN_CN), CNShort), "带中文的年月日格式日期不匹配");
    }

    /**
     * 测试日期转字符串
     */
    @Test
    void dateToString() {
        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = timeFormat.format(date);

        Assert.notNull(DateUtil.dateToString(date, DateUtil.PATTERN_DATE), dateStr);
        Assert.notNull(DateUtil.dateToString(date, DateUtil.PATTERN_TIME), timeStr);
    }

    /**
     * 测试获取年份方法
     */
    @Test
    void getyear() {
        System.out.println(DateUtil.getCurrYear());
    }
}