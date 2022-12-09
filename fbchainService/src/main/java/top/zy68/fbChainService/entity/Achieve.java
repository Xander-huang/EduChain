package top.zy68.fbChainService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * @ClassName Achieve
 * @Description 成就实体对象
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Achieve implements Serializable {
    private static final long serialVersionUID = 380354933872201425L;
    /**
     * Primary Key
     */
    private Integer id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 教育记录ID
     */
    private String eduId;
    /**
     * 成就标题
     */
    private String title;
    /**
     * 获得时间
     */
    private Date acquireTime;
    /**
     * 类型
     */
    private String type;
    /**
     * 备注
     */
    private String remark;
    /**
     * 证明资料
     */
    private String certifyUri;
    /**
     * 审核状态0/1/2
     */
    private Short auditStatus;
    /**
     * 审核反馈
     */
    private String feedback;
}

