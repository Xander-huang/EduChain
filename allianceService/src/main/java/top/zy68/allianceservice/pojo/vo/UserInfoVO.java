package top.zy68.allianceservice.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName UserInfoVO
 * @Description 用户信息视图对象
 * @Author Sustart
 * @Date2022/3/16 15:40
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfoVO {
    /**
     * 联盟点名称
     */
    private String nodeName;
    /**
     * 联盟点类型
     */
    private Short type;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 角色
     */
    private String role;
}
