package top.zy68.allianceservice.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName EduVO
 * @Description 查询教育经历
 * @Author Sustart
 * @Date2022/3/19 21:39
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EduVO {
    private String eduId;

    private String type;

    private String major;
}
