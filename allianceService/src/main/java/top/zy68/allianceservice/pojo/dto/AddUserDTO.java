package top.zy68.allianceservice.pojo.dto;

import lombok.Data;

/**
 * @ClassName AddUserDTO
 * @Description 新增用户DTO
 * @Author Sustart
 * @Date2022/3/16 16:53
 * @Version 1.0
 **/
@Data
public class AddUserDTO {
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号码
     */
    private String phoneNumber;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 角色
     */
    private String role;
}
