package top.zy68.allianceservice.util;

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
    public static final String REGEX_NUMBER = "^[0-9]*$";

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
     * 校验是否是合法用户账号
     *
     * @param account 用户账号
     * @return 是否合法
     */
    public static boolean isLegalAccount(String account) {
        int accountLength = 7;
        if (account != null && account.length() == accountLength && !"".equals(account.trim())) {
            return account.matches(REGEX_NUMBER);
        }
        return false;
    }
}
