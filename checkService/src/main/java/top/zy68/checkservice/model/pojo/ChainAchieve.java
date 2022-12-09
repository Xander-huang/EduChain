package top.zy68.checkservice.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName ChainAchieve
 * @Description 上链的成就对象
 * @Author Sustart
 * @Date2022/4/9 20:03
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class ChainAchieve {
    private String title;
    private String acquireTime;
    private String certifyFile;
}
