package top.zy68.fbChainService.model.chainMainInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseMainInfo {
    /**
     * 教育经历ID
     */
    private String eduId;
    /**
     * 课程列表，长度为3：
     * 列表1：课程名称
     * 列表2：课程学分
     * 列表3：课程证书
     */
    private List<String> nameList;
    private List<String> creditList;
    private List<String> certifyFileList;
}
