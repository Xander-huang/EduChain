package top.zy68.fbChainService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.util.pattern.PathPattern;

import java.io.Serializable;


/**
 * @ClassName PersonUser
 * @Description 个人端用户实体对象
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PersonUser implements Serializable {
    private static final long serialVersionUID = 495063979381298857L;
    /**
     * 身份证号
     */
    private String personId;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐值
     */
    private String salt;
    /**
     * business user id
     */
    private String signUserId;
    /**
     * app that user belong to
     */
    //private String appId;
    private String address;
    private String publicKey;
    private String privateKey;
    private String description;
    /**
     * 0 is standard, 1 is guomi
     */
    private Integer encryptType =0;

    private Integer type = 0;

    private String name;
}

