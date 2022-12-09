package top.zy68.fbChainService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @ClassName CourseScore
 * @Description 课程成绩实体对象
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CourseScore implements Serializable {
    private static final long serialVersionUID = 183952315524985854L;
    /**
     * Primary Key
     */
    private Integer id;
    /**
     * 课程编号
     */
    private String courseId;
    /**
     * 教育记录ID
     */
    private String eduId;
    /**
     * 成绩
     */
    private Short score;
    /**
     * 证明资料
     */
    private String certifyUri;

    /**
     * 连接课程表
     */
    private Course course;
}

