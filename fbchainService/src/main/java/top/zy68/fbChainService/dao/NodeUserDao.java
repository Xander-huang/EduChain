package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import top.zy68.fbChainService.entity.NodeUser;

import java.util.List;
import java.util.Set;

/**
 * @ClassName NodeUserDao
 * @Description 联盟点用户DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface NodeUserDao {
    /**
     * 插入一个联盟点用户
     *
     * @param nodeUser 联盟点用户对象
     * @return 影响的行数
     */
    int insertNodeUser(NodeUser nodeUser);

    /**
     * 通过联盟编号查询联盟点内所有用户
     *
     * @param nodeId 联盟点编号
     * @return 用户列表
     */
    List<NodeUser> queryAllByNodeId(Integer nodeId);

    /**
     * 通过账号删除用户
     *
     * @param account 账户
     * @return 影响行数
     */
    int deleteByAccount(String account);

    /**
     * 通过账号查询用户信息
     *
     * @param account 账户
     * @return 用户对象
     */
    NodeUser queryByAccount(String account);

    /**
     * 通过账号account更新用户信息
     *
     * @param nodeUser 联盟用户
     * @return 影响行数
     */
    int updateNodeUserByAccount(NodeUser nodeUser);

    /**
     * 查询联盟点内所有用户的账号
     *
     * @param nodeId 联盟点编号
     * @return 账号集合
     */
    Set<String> queryAllNodeAccountByNodeId(Integer nodeId);

    /**
     * 通过用户邮箱查询用户信息
     *
     * @param email 邮箱
     * @return 用户对象
     */
    NodeUser queryUserByMail(String email);
}

