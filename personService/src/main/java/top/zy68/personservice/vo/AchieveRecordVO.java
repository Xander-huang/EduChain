package top.zy68.personservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @ClassName AchieveRecordVO
 * @Description 成就经历的dto
 * @Author Sustart
 * @Date2022/3/8 13:48
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AchieveRecordVO {
    private Integer id;
    private String title;
    private String type;
    private Short auditStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String createTime;
    private String certifyUri;
    private String feedback;
}
