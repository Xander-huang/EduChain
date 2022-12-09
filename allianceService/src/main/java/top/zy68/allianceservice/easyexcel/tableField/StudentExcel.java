package top.zy68.allianceservice.easyexcel.tableField;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @ClassName StudentExcel
 * @Description excel导入学生表头
 * @Author Sustart
 * @Date2022/2/18 15:04
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExcel {

    @ExcelProperty("身份证号")
    private String personId;
    @ExcelProperty("专业编号")
    private String majorId;
    @ExcelProperty("专业名称")
    private String majorName;
}
