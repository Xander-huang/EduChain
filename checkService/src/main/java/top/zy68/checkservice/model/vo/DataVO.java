package top.zy68.checkservice.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName DataVO
 * @Description 封装视图
 * @Author Sustart
 * @Date2022/4/9 19:23
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataVO {
    private BasicVO basic;
    private List<EducationVO> education;
}
