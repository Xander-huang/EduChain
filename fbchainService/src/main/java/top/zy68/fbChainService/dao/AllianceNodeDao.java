package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.entity.AllianceNode;


import java.util.Set;

/**
 * @ClassName AllianceNodeDao
 * @Description 联盟点DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface AllianceNodeDao {
    /**
     * 通过联盟点编号查询联盟点信息
     *
     * @param nodeId 编号
     * @return 联盟点对象
     */
    AllianceNode queryByNodeId(Integer nodeId);

    /**
     * 插入一个联盟点
     *
     * @param nodeId   联盟点编号
     * @param nodeName 联盟点名称
     * @param type     联盟点类型
     * @return 影响行数
     */
    int insertNode(@Param("nodeId") Integer nodeId, @Param("nodeName") String nodeName, @Param("type") Integer type);

    /**
     * 查询所有联盟点编号
     *
     * @return 联盟点编号集合
     */
    Set<Integer> queryAllNodeId();
}

