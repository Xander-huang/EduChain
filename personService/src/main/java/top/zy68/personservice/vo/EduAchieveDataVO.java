package top.zy68.personservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName EduAchieveDataVO
 * @Description
 * @Author Sustart
 * @Date2022/4/10 23:46
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EduAchieveDataVO {
    private String eduId;
    private List<AchieveVO> achieve;
}
