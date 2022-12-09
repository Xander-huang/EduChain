package top.zy68.allianceservice.pojo.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @ClassName RegisterDTO
 * @Description 封装注册信息对象
 * @Author Sustart
 * @Date2022/1/19 14:36
 * @Version 1.0
 **/
@Data
public class NodeRegisterDTO implements Serializable {
    private static final long serialVersionUID = -87873153792190321L;
    /**
     * 联盟点名称
     */
    private String nodeName;
    /**
     * 联盟点类型0/1
     */
    private Integer type;

    /**
     * 姓名
     */
    private String userName;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 8, max = 12, message = "密码长度须为8~16个字符")
    private String password;
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误")
    @NotEmpty(message = "邮箱不能为空")
    private String email;
    /**
     * 手机号码
     */
    @NotEmpty(message = "电话号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话号码有误")
    private String phone;
    /**
     * 邮箱验证码
     */
    @NotEmpty(message = "邮箱验证码不能为空")
    private String captcha;
}
