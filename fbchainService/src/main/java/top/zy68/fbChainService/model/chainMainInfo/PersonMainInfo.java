package top.zy68.fbChainService.model.chainMainInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName PersonMainInfo
 * @Description 上链的个人信息
 * @Author Sustart
 * @Date2022/4/6 20:26
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class PersonMainInfo {
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
    private String birthday;
}
