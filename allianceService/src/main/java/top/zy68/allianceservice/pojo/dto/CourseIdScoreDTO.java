package top.zy68.allianceservice.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CourseIdScoreDTO
 * @Description
 * @Author Sustart
 * @Date2022/4/16 19:24
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseIdScoreDTO {
    @JsonProperty(value = "courseId")
    private String courseId;
    @JsonProperty(value = "score")
    private Short score;
}
