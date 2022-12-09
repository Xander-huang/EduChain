package top.zy68.fbChainService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ScoreDTO
 * @Description
 * @Author Sustart
 * @Date2022/5/20 16:05
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDTO {
    /**
     * 教育记录ID
     */
    private String eduId;
    /**
     * 课程编号
     */
    private String courseId;
    /**
     * 成绩
     */
    private Short score;
}
