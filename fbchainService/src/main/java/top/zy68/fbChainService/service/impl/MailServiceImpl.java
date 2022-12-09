package top.zy68.fbChainService.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import top.zy68.fbChainService.config.Mail;
import top.zy68.fbChainService.service.MailService;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.Objects;

/**
 * @ClassName MailServiceImpl
 * @Description 邮件业务服务实现类
 * @Author Sustart
 * @Date2022/1/6 16:05
 * @Version 1.0
 **/
@Service("mailService")
@Slf4j
public class MailServiceImpl implements MailService {

    /**
     * 引入 spring-boot-mail 包中的关键邮件服务实现类
     */
    @Resource
    private JavaMailSenderImpl mailSender;

    /**
     * 发送邮件（支持MailDO中的所有内容）
     *
     * @param mailDO 封装的邮件服务信息
     * @return 邮件服务信息
     */
    @Override
    public Mail sendMail(Mail mailDO) {
        try {
            // 1. 检测必须的信息是否都存在
            checkMail(mailDO);
            // 2. 发送邮件
            sendMimeMail(mailDO);
        } catch (Exception e) {
            log.error("发送邮件失败：", e);
            mailDO.setStatus(false);
            mailDO.setError("发送邮件失败：" + e.getMessage());
        }
        return mailDO;
    }

    /**
     * 检测邮件对象必须的信息是否都存在的方法
     *
     * @param mailDO 邮件对象
     */
    private void checkMail(Mail mailDO) {
        if (Objects.isNull(mailDO.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (Objects.isNull(mailDO.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (Objects.isNull(mailDO.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    /**
     * 实际的邮件发送方法
     *
     * @param mailDO 邮件对象
     */
    private void sendMimeMail(Mail mailDO) {
        try {
            // 构造一个复杂邮件发送
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            mimeMessageHelper.setFrom(mailDO.getFrom());
            mimeMessageHelper.setTo(mailDO.getTo().split(","));
            mimeMessageHelper.setSubject(mailDO.getSubject());
            mimeMessageHelper.setText(mailDO.getText());

            // 抄送
            if (!Objects.isNull(mailDO.getCc())) {
                mimeMessageHelper.setCc(mailDO.getCc().split(","));
            }
            // 密送
            if (!Objects.isNull(mailDO.getBcc())) {
                mimeMessageHelper.setCc(mailDO.getBcc().split(","));
            }
            //附件
            if (!Objects.isNull(mailDO.getFiles())) {
                // 多个附件
                for (File file : mailDO.getFiles()) {
                    mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getName()), file);
                }
            }
            // 发送时间
            if (Objects.isNull(mailDO.getSentDate())) {
                mailDO.setSentDate(new Date());
            }
            mimeMessageHelper.setSentDate(mailDO.getSentDate());
            mailSender.send(mimeMessageHelper.getMimeMessage());
            mailDO.setStatus(true);
            log.info("发送邮件成功：{} -> {}", mailDO.getFrom(), mailDO.getTo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
