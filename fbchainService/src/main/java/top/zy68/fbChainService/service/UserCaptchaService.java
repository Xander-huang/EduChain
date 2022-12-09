package top.zy68.fbChainService.service;


import top.zy68.fbChainService.entity.UserCaptcha;

/**
 * @ClassName UserCaptchaService
 * @Description UserCaptchaService
 * @Author Sustart
 * @Date2022/2/16 13:31
 * @Version 1.0
 **/
public interface UserCaptchaService {

    UserCaptcha selectOneByCaptcha(String captcha);
}
