package top.zy68.personservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName CourseBO
 * @Description 封装的一个课程业务类，包含课程主要信息
 * @Author Sustart
 * @Date2022/3/6 16:54
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CourseBO {
    /**
     * 课程ID
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
    /**
     * 课程证书
     */
    private String certifyUri;
}
