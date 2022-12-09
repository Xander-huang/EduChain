package top.zy68.fbChainService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @ClassName Major
 * @Description 专业实体对象
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Major implements Serializable {
    private static final long serialVersionUID = -41638189214054334L;
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
    /**
     * 毕业学分
     */
    private Integer graduateCredit;
}

