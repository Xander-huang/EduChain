package top.zy68.fbChainService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @ClassName Course
 * @Description 课程实体对象
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Course implements Serializable {
    private static final long serialVersionUID = 103542474003687558L;
    /**
     * 课程编号
     */
    private String courseId;
    /**
     * 课程名称
     */
    private String name;
    /**
     * 学分
     */
    private Short credit;
}

