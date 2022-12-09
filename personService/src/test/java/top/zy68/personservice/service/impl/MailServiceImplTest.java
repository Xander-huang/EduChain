package top.zy68.personservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.personservice.pojo.MailDO;
import top.zy68.personservice.service.MailDOService;

import java.io.File;

/**
 * @ClassName MailServiceImplTest
 * @Description 测试邮件发送服务业务
 * @Author Sustart
 * @Date2022/1/6 17:18
 * @Version 1.0
 **/
@SpringBootTest
class MailServiceImplTest {

    @Autowired
    MailDOService mailService;

    /**
     * 测试邮件服务业务
     */
    // @Test
    void sendMail() {
        MailDO mailDO = new MailDO();

        mailDO.setFrom("415229929@qq.com");
        mailDO.setTo("zymail68@foxmail.com");
        mailDO.setSubject("邮件业务测试");
        mailDO.setText("简单测试");

        String filePath1 = "F:\\IdeaProjects\\FBTeachingService\\common\\common.iml";
        String filePath2 = "F:\\IdeaProjects\\FBTeachingService\\common\\pom.xml";
        File attachment1 = new File(filePath1);
        File attachment2 = new File(filePath2);
        File[] files = new File[]{attachment1, attachment2};

        mailDO.setFiles(files);
        mailService.sendMail(mailDO);
    }
}