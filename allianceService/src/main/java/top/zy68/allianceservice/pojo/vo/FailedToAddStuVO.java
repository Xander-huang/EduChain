package top.zy68.allianceservice.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName FailedToAddStuVO
 * @Description 封装批量添加学生时添加失败的学生信息
 * @Author Sustart
 * @Date2022/3/29 14:08
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class FailedToAddStuVO {
    /**
     * 身份证号
     */
    private String personId;
    /**
     * 专业编号
     */
    private String majorId;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 失败原因
     */
    private String failedReason;
}
