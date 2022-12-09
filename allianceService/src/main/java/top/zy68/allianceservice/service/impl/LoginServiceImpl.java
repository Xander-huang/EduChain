package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.service.LoginService;
import top.zy68.allianceservice.util.JwtUtil;
import top.zy68.allianceservice.util.ValidateCodeUtil;
import top.zy68.fbChainService.config.Mail;
import top.zy68.fbChainService.dao.NodeUserDao;
import top.zy68.fbChainService.entity.NodeUser;
import top.zy68.fbChainService.service.MailService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginServiceImpl
 * @Description 登录服务
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Service("LoginService")
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Resource
    Mail mailDO;
    @Resource
    MailService mailService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    NodeUserDao nodeUserDao;


    /**
     * 获取登录时的图片验证码
     *
     * @return 包含图片的Base64编码和验证码字符串
     */
    @Override
    public Map<String, String> getLoginCaptcha() {
        String captcha = null;
        String image = null;

        try {
            captcha = ValidateCodeUtil.getCaptcha();
            image = ValidateCodeUtil.getCaptchaImg(captcha);
        } catch (Exception e) {
            log.error("生成验证码图片异常！");
            e.printStackTrace();
        }

        Map<String, String> resData = new HashMap<>(2);
        resData.put("img", image);
        resData.put("imgKey", captcha);

        return resData;
    }

    /**
     * 给指定邮箱发送验证邮箱的邮件
     *
     * @param email 邮箱
     */
    @Override
    public void sendEmailVerifyCode(String email) {
        // 获取验证码
        String captcha = ValidateCodeUtil.getCaptcha();
        String subject = "终身教育服务平台邮箱验证";
        String text = "先生/女士，您好：\n您的邮箱验证码是：" + captcha + "，有效时间为 5 分钟，请尽快处理。注：如非本人操作，请忽略！";

        mailDO.setTo(email);
        mailDO.setSubject(subject);
        mailDO.setText(text);
        mailService.sendMail(mailDO);

        // 发送失败
        if (!mailDO.getStatus()) {
            log.warn("Failed to send a mail to : " + email);
            throw new RuntimeException(mailDO.getError());
        }
        log.info("Succeed to send a mail to : " + email);

        // redis保存验证码用于校验，五分钟内有效。
        stringRedisTemplate.opsForValue().set(email, captcha, 5, TimeUnit.MINUTES);
    }

    /**
     * 忘记密码：通过邮箱修改用户密码
     *
     * @param email   邮箱
     * @param newPwd  新密码
     * @param captcha 验证码
     */
    @Override
    public void resetUserPwd(String email, String newPwd, String captcha) {
        NodeUser nodeUser = nodeUserDao.queryUserByMail(email);
        if (nodeUser == null) {
            throw new ClientBusinessException("不存在该用户");
        }

        String cacheCaptcha = stringRedisTemplate.opsForValue().get(email);
        if (cacheCaptcha == null || !Objects.equals(cacheCaptcha, captcha)) {
            throw new ClientBusinessException("验证码不存在或已过期.");
        }

        // 存储新密码
        String salt = JwtUtil.generateSalt();
        String newEncryptPwd = new Sha256Hash(newPwd, salt).toHex();
        nodeUser.setPassword(newEncryptPwd);
        nodeUser.setSalt(salt);

        stringRedisTemplate.delete(email);
        int row = nodeUserDao.updateNodeUserByAccount(nodeUser);
        if (row != 1) {
            log.info("更改用户信息失败.");
            throw new ClientBusinessException("更改失败");
        }
        log.info("成功更改用户密码.");
    }
}
