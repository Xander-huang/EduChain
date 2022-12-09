package top.zy68.allianceservice.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @ClassName CourseIdScoreDTO
 * @Description
 * @Author Sustart
 * @Date2022/4/16 19:24
 * @Version 1.0
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StuCourseScoreDTO {
    @JsonProperty(value = "stuId")
    private String stuId;
    @JsonProperty(value = "course")
    private List<CourseIdScoreDTO> course;
}

