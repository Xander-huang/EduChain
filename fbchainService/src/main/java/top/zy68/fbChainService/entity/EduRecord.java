package top.zy68.fbChainService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @ClassName EduRecord
 * @Description 教育记录实体对象
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EduRecord implements Serializable {
    private static final long serialVersionUID = 279065900691809024L;
    /**
     * 教育记录ID
     */
    private String eduId;
    /**
     * 身份证号
     */
    private String personId;
    /**
     * 教育类型
     */
    private String type;
    /**
     * 入学时间
     */
    private Integer beginTime;
    /**
     * 毕业时间
     */
    private Integer endTime;
    /**
     * 联盟点编号
     */
    private Integer nodeId;
    /**
     * 联盟点名称
     */
    private String nodeName;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 获得学分
     */
    private Integer acquireCredit;
    /**
     * 毕业学分
     */
    private Integer graduateCredit;
    /**
     * 证明资料
     */
    private String certifyUri;
    /**
     * 教育经历上链交易哈希
     */
    private String eduTransactionHash;
    /**
     * 课程信息上链交易哈希
     */
    private String coursesTransactionHash;
    /**
     * 成就信息上链交易哈希
     */
    private String achievesTransactionHash;
}

