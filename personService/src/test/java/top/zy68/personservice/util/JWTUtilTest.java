package top.zy68.personservice.util;

import org.junit.jupiter.api.Test;
import top.zy68.personservice.utils.JwtUtil;

/**
 * @ClassName JWTUtilTest
 * @Description 测试 JwtUtil
 * @Author Sustart
 * @Date2022/1/8 21:14
 * @Version 1.0
 **/

public class JWTUtilTest {

    private String idNumber = "4514810979110503031";
    private String salt = "salt..";
    private long time = 2L;

    @Test
    void verify() {
        String token = JwtUtil.sign(idNumber, salt, time);
        System.out.println(token);

        System.out.println(JwtUtil.verify(token, idNumber, salt));

        System.out.println(JwtUtil.getPersonId(token));
    }

    /**
     * 测试过期的token是否还可以获取负载信息？结论：可以。
     * 测试过期的token校验是否合法？结论：不合法。
     */
    @Test
    void expiredTokenGetIdNumber() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZE51bWJlciI6IjQ1MTQ4MTA5NzkxMTA1MDMwMzEiLCJleHAiOjE2NDY5NzQ3NTcsImlhdCI6MTY0Njk3NDc1NX0.I2ado6B01HzYWMSKv5qHOVOUWGw_BbjgcZ-OVaPss2I";
        System.out.println(JwtUtil.getPersonId(token));
        System.out.println(JwtUtil.verify(token, idNumber, salt));
    }

}