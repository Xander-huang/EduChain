package top.zy68.checkservice.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName ChainCourse
 * @Description 上链的课程对象
 * @Author Sustart
 * @Date2022/4/9 20:00
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class ChainCourse {
    private String name;
    private String credit;
    private String certifyFile;
}
