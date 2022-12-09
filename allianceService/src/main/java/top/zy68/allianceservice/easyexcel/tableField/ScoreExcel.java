package top.zy68.allianceservice.easyexcel.tableField;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @ClassName ScoreExcel
 * @Description excel导入课程成绩表头：通过 身份证号+专业 确定 学号
 * @Author Sustart
 * @Date2022/4/11 16:04
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreExcel {

    @ExcelProperty("身份证号")
    private String personId;

    @ExcelProperty("专业编号")
    private String majorId;

    @ExcelProperty("课程编号")
    private String courseId;

    @ExcelProperty("课程成绩")
    private Integer courseScore;
}
