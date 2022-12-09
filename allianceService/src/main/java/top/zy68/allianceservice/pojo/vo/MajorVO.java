package top.zy68.allianceservice.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName MajorVO
 * @Description 专业的视图对象
 * @Author Sustart
 * @Date2022/3/29 19:32
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class MajorVO {
    /**
     * 专业编号
     */
    private String majorId;
    /**
     * 专业名称
     */
    private String name;
    /**
     * 教育类型
     */
    private String type;
}
