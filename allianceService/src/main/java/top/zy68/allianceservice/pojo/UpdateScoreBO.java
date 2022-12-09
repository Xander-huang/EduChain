package top.zy68.allianceservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.zy68.fbChainService.entity.Student;

/**
 * @ClassName UpdateScoreBO
 * @Description
 * @Author Sustart
 * @Date2022/4/11 21:59
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScoreBO {
    private Student student;
    private String courseId;
    private Integer score;
}
