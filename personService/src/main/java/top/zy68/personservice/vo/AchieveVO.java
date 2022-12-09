package top.zy68.personservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AchieveVO
 * @Description
 * @Author Sustart
 * @Date2022/4/10 22:05
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchieveVO {
    private Integer id;
    private String title;
    private String acquireTime;
    private String type;
}
