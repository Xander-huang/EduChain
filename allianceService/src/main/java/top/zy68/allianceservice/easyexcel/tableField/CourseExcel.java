package top.zy68.allianceservice.easyexcel.tableField;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName CourseExcel
 * @Description 课程信息表头
 * @Author Sustart
 * @Date2022/2/18 15:53
 * @Version 1.0
 **/
@Data
public class CourseExcel {
    /**
     * 课程名称
     */
    @ExcelProperty("课程名称")
    private String courseName;
    /**
     * 学分
     */
    @ExcelProperty("学分")
    private Short credit;
}
