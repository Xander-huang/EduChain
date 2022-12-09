package top.zy68.personservice.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName UserInfoVO
 * @Description 个人主要信息
 * @Author Sustart
 * @Date2022/3/5 22:42
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
public class UserInfoVO {
    /**
     * 身份证号
     */
    private String personId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 民族
     */
    private String nation;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 住址
     */
    private String address;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
}
