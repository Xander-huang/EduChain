package top.zy68.fbChainService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * @ClassName Person
 * @Description 个人实体对象
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Person implements Serializable {
    private static final long serialVersionUID = -19433419514400385L;
    /**
     * 身份证号
     */
    private String personId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 民族
     */
    private String nation;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 住址
     */
    private String address;
    /**
     * 交易哈希
     */
    private String transactionHash;
}

