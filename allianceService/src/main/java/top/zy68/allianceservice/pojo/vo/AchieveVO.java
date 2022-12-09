package top.zy68.allianceservice.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName AchieveVO
 * @Description 用于查询所有学生的所有成就的VO
 * @Author Sustart
 * @Date2022/3/19 21:39
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AchieveVO {
    /**
     * 成就ID
     */
    private Integer id;
    private String eduId;
    private String personId;
    private String name;
    private String majorName;
    private String eduType;
    private String acquireTime;
    /**
     * 成就类型
     */
    private String type;
    /**
     * 成就标题
     */
    private String title;
    /**
     * 备注
     */
    private String remark;
    /**
     * 审核状态
     */
    private Short auditStatus;
    /**
     * 审核反馈
     */
    private String feedback;
    /**
     * 成就证明文件
     */
    private String certifyUri;
}
