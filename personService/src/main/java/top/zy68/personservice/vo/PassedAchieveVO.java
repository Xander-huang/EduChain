package top.zy68.personservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName PassedAchieveVO
 * @Description
 * @Author Sustart
 * @Date2022/4/10 22:04
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassedAchieveVO {
    private String eduId;
    private List<AchieveVO> achieve;
}
