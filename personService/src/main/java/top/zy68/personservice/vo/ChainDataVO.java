package top.zy68.personservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ChainDataVO
 * @Description 封装视图
 * @Author Sustart
 * @Date2022/4/9 19:23
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChainDataVO {
    private ChainBasicVO basic;
    private List<ChainEducationVO> education;
}
