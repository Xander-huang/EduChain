package top.zy68.personservice.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import top.zy68.personservice.pojo.CourseBO;

import java.util.List;

/**
 * @ClassName EduRecordScoreVO
 * @Description 教育记录及成绩的复杂VO
 * @Author Sustart
 * @Date2022/3/6 16:50
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
public class EduRecordScoreVO {
    /**
     * 教育记录
     */
    private String eduId;
    /**
     * 教育类型
     */
    private String type;
    /**
     * 入学时间
     */
    private Integer beginTime;
    /**
     * 毕业时间
     */
    private Integer endTime;
    /**
     * 已获学分
     */
    private Integer acquireCredit;
    /**
     * 毕业学分
     */
    private Integer graduateCredit;
    /**
     * 联盟点名称
     */
    private String nodeName;
    /**
     * 专业类型
     */
    private String majorName;

    /**
     * 所有课程成绩
     */
    List<CourseBO> course;
}
