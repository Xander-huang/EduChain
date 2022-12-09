package top.zy68.allianceservice.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName StudentInfoVO
 * @Description 学生主要信息VO
 * @Author Sustart
 * @Date2022/3/27 14:07
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
public class StudentInfoVO {
    /**
     * 学号=教育记录ID
     */
    private String stuId;
    /**
     * 身份证号
     */
    private String personId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 教育类型
     */
    private String eduType;
    /**
     * 是否已上链0/1
     */
    private Short uplinked;
}
