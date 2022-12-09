package top.zy68.fbChainService.model.chainMainInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchieveMainInfo {
    /**
     * 教育经历ID
     */
    private String eduId;
    /**
     * 三个列表长度要保持一致
     * 列表1：成就title
     * 列表2：获得时间
     * 列表3：证书文件名称
     */
    private List<String> titleList;
    private List<String> acquireTimeList;
    private List<String> certifyFileList;
}
