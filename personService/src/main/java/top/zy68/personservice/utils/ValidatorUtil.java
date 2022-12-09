package top.zy68.personservice.utils;

import java.util.regex.Pattern;

/**
 * @ClassName ValidatorUtil
 * @Description 参数校验工具类
 * @Author Sustart
 * @Date2022/3/14 23:57
 * @Version 1.0
 **/
public class ValidatorUtil {
    public static final String REGEX_MAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final String REGEX_ID_NUMBER = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    /**
     * 校验是否是合法邮箱
     *
     * @param mail 邮箱
     * @return 是否合法
     */
    public static boolean isLegalMail(String mail) {
        return Pattern.matches(REGEX_MAIL, mail);
    }

    /**
     * 校验是否是合法身份证号
     *
     * @param personId 身份证号
     * @return 是否合法
     */
    public static boolean isLegalPersonId(String personId) {
        return Pattern.matches(REGEX_ID_NUMBER, personId);
    }
}
