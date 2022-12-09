package top.zy68.personservice.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.personservice.utils.ValidateCodeUtil;

/**
 * @ClassName ValidateCodeUtilTest
 * @Description 测试生成验证码图片的base64编码
 * @Author Sustart
 * @Date2021/12/22 16:22
 * @Version 1.0
 **/
@SpringBootTest
class ValidateCodeUtilTest {

    /**
     * 测试生成验证码 及图片编码
     */
    @Test
    public void getValidateCodeTest() {
        String captcha = ValidateCodeUtil.getCaptcha();
        System.out.println(captcha);
        System.out.println(ValidateCodeUtil.getCaptchaImg(captcha));
    }

}