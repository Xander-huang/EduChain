package top.zy68.allianceservice.service;

import java.util.Map;

/**
 * @ClassName LoginService
 * @Description 登录服务
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
public interface LoginService {
    /**
     * 获取登录时的图片验证码
     *
     * @return 包含图片的Base64编码和验证码字符串
     */
    Map<String, String> getLoginCaptcha();

    /**
     * 给指定邮箱发送验证邮箱的邮件
     *
     * @param email 邮箱
     */
    void sendEmailVerifyCode(String email);

    /**
     * 忘记密码：通过邮箱修改用户密码
     *
     * @param email   邮箱
     * @param newPwd  新密码
     * @param captcha 验证码
     */
    void resetUserPwd(String email, String newPwd, String captcha);
}
