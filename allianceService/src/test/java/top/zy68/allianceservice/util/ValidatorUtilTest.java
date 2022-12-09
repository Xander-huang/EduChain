package top.zy68.allianceservice.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName ValidatorUtilTest
 * @Description
 * @Author Sustart
 * @Date2022/3/29 21:15
 * @Version 1.0
 **/
class ValidatorUtilTest {

    @Test
    void isLegalMail() {
    }

    @Test
    void isLegalAccount() {
        String account = "1000089";
        if (ValidatorUtil.isLegalAccount(account)) {
            System.out.println("合法");
        } else {
            System.out.println("不合法");
        }
    }
}