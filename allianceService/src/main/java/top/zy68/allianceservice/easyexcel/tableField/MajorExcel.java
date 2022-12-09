package top.zy68.allianceservice.easyexcel.tableField;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName CourseExcel
 * @Description 课程信息表头
 * @Author Sustart
 * @Date2022/3/26 21:53
 * @Version 1.0
 **/
@Data
public class MajorExcel {
    /**
     * 专业名称
     */
    @ExcelProperty("专业名称")
    private String majorName;
    /**
     * 教育类型
     */
    @ExcelProperty("教育类型")
    private String type;
}
