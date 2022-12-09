package top.zy68.fbChainService.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.zy68.fbChainService.service.UserCaptchaService;
import top.zy68.fbChainService.dao.UserCaptchaDao;
import top.zy68.fbChainService.entity.UserCaptcha;

import javax.annotation.Resource;


/**
 * @ClassName ReproxyService
 * @Description ReproxyService
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("userCaptchaService")
public class UserCaptchaServiceImpl implements UserCaptchaService {

    @Resource
    UserCaptchaDao userCaptchaDao;


    @Override
    public UserCaptcha selectOneByCaptcha(String captcha) {
        UserCaptcha userCaptcha = userCaptchaDao.selectOneByCaptcha(captcha);
        return userCaptcha;
    }
}
