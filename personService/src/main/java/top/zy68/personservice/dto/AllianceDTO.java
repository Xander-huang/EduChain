package top.zy68.personservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName AllianceDTO
 * @Description 教育记录对应的联盟点信息
 * @Author Sustart
 * @Date2022/3/8 14:58
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AllianceDTO {
    private String eduId;
    private String nodeName;
    private String type;
    private String majorName;
}
