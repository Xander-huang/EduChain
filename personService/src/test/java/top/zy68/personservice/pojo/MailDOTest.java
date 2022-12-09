package top.zy68.personservice.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @ClassName MailDOTest
 * @Description 测试邮件发送
 * @Author Sustart
 * @Date2022/1/6 16:10
 * @Version 1.0
 **/
@SpringBootTest
class MailDOTest {

    @Autowired
    private JavaMailSenderImpl mailSender;

    /**
     * 测试直接配置方式的邮件发送功能
     */
    // @Test
    public void sendMail() {
        //简单邮件
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("415229929@qq.com");
        simpleMailMessage.setTo("zymail68@foxmail.com");
        simpleMailMessage.setSubject("Happy New Year");
        simpleMailMessage.setText("测试成功！");
        mailSender.send(simpleMailMessage);
    }

    /**
     * 测试直接配置方式的带附件的邮件发送
     *
     * @throws MessagingException 消息发送异常
     */
    // @Test
    public void sendMailWithAttachmentTest() throws MessagingException {
        String filePath = "F:\\IdeaProjects\\FBTeachingService\\common\\pom.xml";

        String from = "415229929@qq.com";
        String to = "zymail68@foxmail.com";
        String subject = "测试带附件邮件发送";
        String text = "看附件";

        File attachment = new File(filePath);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment(attachment.getName(), attachment);
        mailSender.send(mimeMessage);
    }


}