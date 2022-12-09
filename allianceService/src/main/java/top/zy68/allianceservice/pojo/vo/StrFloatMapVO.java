package top.zy68.allianceservice.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName StrIntMapVO
 * @Description
 * @Author Sustart
 * @Date2022/4/11 11:42
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrFloatMapVO {
    private List<String> subscript;
    private List<Float> data;
}
