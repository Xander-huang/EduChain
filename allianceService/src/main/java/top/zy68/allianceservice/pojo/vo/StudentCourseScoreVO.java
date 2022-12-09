package top.zy68.allianceservice.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName StudentCourseScoreVO
 * @Description 学生课程成绩
 * @Author Sustart
 * @Date2022/3/19 21:39
 * @Version 1.0
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StudentCourseScoreVO {
    private String stuId;

    private String personId;
    /**
     * 学生姓名
     */
    private String name;

    private String majorName;

    private String eduType;

    private Short isUplinked;

    private List<CourseScoreVO> course;

}
