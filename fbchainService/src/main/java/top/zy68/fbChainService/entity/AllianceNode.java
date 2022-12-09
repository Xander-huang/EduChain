package top.zy68.fbChainService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @ClassName AllianceNode
 * @Description 联盟点实体对象
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AllianceNode implements Serializable {
    private static final long serialVersionUID = -87873153792190321L;
    /**
     * 联盟点编号
     */
    private Integer nodeId;
    /**
     * 联盟点名称
     */
    private String name;
    /**
     * 联盟点类型0/1
     */
    private Short type;
}

