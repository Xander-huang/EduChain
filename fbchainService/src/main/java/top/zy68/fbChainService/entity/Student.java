package top.zy68.fbChainService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @ClassName Student
 * @Description 学生实体对象
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Student implements Serializable {
    private static final long serialVersionUID = -68222416111775390L;
    /**
     * 姓名
     */
    private String name;
    /**
     * 学号=教育记录ID
     */
    private String stuId;
    /**
     * 身份证号
     */
    private String personId;
    /**
     * 教育类型
     */
    private String eduType;
    /**
     * 是否已上链0/1
     */
    private Short uplinked;
}

