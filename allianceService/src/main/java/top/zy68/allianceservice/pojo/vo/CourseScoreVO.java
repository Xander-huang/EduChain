package top.zy68.allianceservice.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName CourseScoreVO
 * @Description 展示课程成绩
 * @Author Sustart
 * @Date2022/3/19 21:39
 * @Version 1.0
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CourseScoreVO {
    private String courseId;

    private String stuId;

    /**
     * 是否已上链0/1
     */
    private Short isUplinked;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 学分
     */
    private Short credit;
    /**
     * 分数
     */
    private Short score;
}
