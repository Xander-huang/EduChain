package top.zy68.personservice.service;


import top.zy68.personservice.pojo.MailDO;

/**
 * @ClassName MailService
 * @Description 邮件业务类
 * @Author Sustart
 * @Date2022/1/6 15:59
 * @Version 1.0
 **/
public interface MailDOService {
    /**
     * 发送邮件（支持MailDO中的所有内容）
     *
     * @param mailDO 封装的邮件服务信息
     * @return 邮件服务信息
     */
    MailDO sendMail(MailDO mailDO);
}
