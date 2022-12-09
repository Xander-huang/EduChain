package top.zy68.fbChainService.service;


import top.zy68.fbChainService.config.Mail;

/**
 * @ClassName MailService
 * @Description 邮件业务类
 * @Author Sustart
 * @Date2022/1/6 15:59
 * @Version 1.0
 **/
public interface MailService {
    /**
     * 发送邮件（支持MailDO中的所有内容）
     *
     * @param mailDO 封装的邮件服务信息
     * @return 邮件服务信息
     */
    Mail sendMail(Mail mailDO);
}
