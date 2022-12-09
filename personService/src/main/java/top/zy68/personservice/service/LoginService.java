package top.zy68.personservice.service;

import top.zy68.personservice.api.ResponseBean;
import top.zy68.personservice.dto.RegisterDTO;
import top.zy68.personservice.pojo.uservo.RspUserInfoVo;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName LoginService
 * @Description 登录模块
 * @Author Sustart
 * @Date2022/3/7 11:33
 * @Version 1.0
 **/
public interface LoginService {
    /**
     * 注册服务
     *
     * @param registerDTO 注册信息
     */
    void register(RegisterDTO registerDTO, HttpServletResponse response) throws Exception;

    /**
     * 给指定邮箱发送验证邮箱的邮件
     *
     * @param mail 邮箱
     */
    void sendEmailVerifyCode(String mail);

    /**
     * 获取登录时的图片验证码
     *
     * @return 包含图片的Base64编码和验证码字符串
     */
    Map<String, String> getLoginCaptcha();
}
