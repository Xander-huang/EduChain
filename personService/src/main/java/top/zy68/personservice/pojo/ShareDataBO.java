package top.zy68.personservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ShareDataBO
 * @Description 用于分享的数据封装对象
 * @Author Sustart
 * @Date2022/4/9 20:26
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class ShareDataBO {
    private String personId;
    private String personTransactionHash;
    private List<String> eduList;
}

