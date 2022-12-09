package top.zy68.personservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @ClassName RegisterDTO
 * @Description 封装注册信息对象
 * @Author Sustart
 * @Date2022/1/19 14:36
 * @Version 1.0
 **/
@Data
@ApiModel
public class RegisterDTO {
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", message = "身份证号格式非法")
    private String personId;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @NotEmpty(message = "姓名不能为空")
    private String name;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    @NotEmpty(message = "性别不能为空")
    private String gender;
    /**
     * 民族
     */
    @ApiModelProperty(value = "民族")
    @NotEmpty(message = "民族不能为空")
    private String nation;
    /**
     * 生日
     */
    @ApiModelProperty(value = "出生日期")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date birthday;
    /**
     * 地址
     */
    @ApiModelProperty(value = "家庭住址")
    @NotEmpty(message = "地址不能为空")
    private String address;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式错误")
    @NotEmpty(message = "邮箱不能为空")
    private String email;
    /**
     * 电话号码
     */
    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "电话号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话号码有误")
    private String phone;
    /**
     * 邮箱验证码
     */
    @ApiModelProperty(value = "邮箱验证码")
    @NotEmpty(message = "邮箱验证码不能为空")
    private String captcha;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 8, max = 12, message = "密码长度须为8~16个字符")
    private String password;


    private Integer encryptType = 0 ;

    private boolean returnPrivateKey = false;

    private Integer type = 0;
}
