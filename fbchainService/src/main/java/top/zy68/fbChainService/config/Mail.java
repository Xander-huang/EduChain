package top.zy68.fbChainService.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

/**
 * @ClassName MailDO
 * @Description 封装邮件相关信息
 * @Author Sustart
 * @Date2022/1/6 15:47
 * @Version 1.0
 **/
@Data
@Component
public class Mail {
    /**
     * 邮件id
     */
    private String id;
    /**
     * 发信方
     */
    @Value("${spring.mail.username}")
    private String from;
    /**
     * 收信方（多个邮箱用逗号“,”隔开）
     */
    private String to;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String text;
    /**
     * 发送时间
     */
    private Date sentDate;
    /**
     * 抄送（多个邮箱用逗号“,”隔开）
     */
    private String cc;
    /**
     * 密送（多个邮箱用逗号“,”隔开）
     */
    private String bcc;
    /**
     * 状态
     */
    private Boolean status;
    /**
     * 报错信息
     */
    private String error;
    /**
     * 附件
     */
    @JsonIgnore
    private File[] files;
}
