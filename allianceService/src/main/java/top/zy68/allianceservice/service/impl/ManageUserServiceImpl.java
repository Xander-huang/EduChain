package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.exception.InternalBusinessException;
import top.zy68.allianceservice.exception.NormalBusinessException;
import top.zy68.allianceservice.pojo.vo.UserVO;
import top.zy68.allianceservice.service.ManageUserService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.allianceservice.util.JwtUtil;
import top.zy68.fbChainService.dao.NodeUserDao;
import top.zy68.fbChainService.entity.NodeUser;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName ManageUserServiceImpl
 * @Description 顶级管理员管理用户的service
 * @Author Sustart
 * @Date2022/3/19 21:16
 * @Version 1.0
 **/
@Slf4j
@Service("ManageUserService")
public class ManageUserServiceImpl implements ManageUserService {

    @Resource
    private NodeUserDao nodeUserDao;

    /**
     * 添加一个用户
     *
     * @param name     姓名
     * @param email    邮箱
     * @param phone    手机号码
     * @param operator 操作者账号
     * @param role     角色
     */
    @Override
    public UserVO insertUser(String operator, String name, String email, String phone, String role) {
        String account = generateAccount(IdUtil.getNodeId(operator));
        if (ObjectUtils.isEmpty(account)) {
            log.warn("用户账号生成失败.");
            throw new NormalBusinessException("用户账号生成失败，请提交重试.");
        }

        String salt = JwtUtil.generateSalt();
        // 密码默认为手机号码后八位
        String password = phone.substring(3);
        String encryptPwd = new Sha256Hash(password, salt).toHex();

        NodeUser nodeUser = new NodeUser()
                .setAccount(account)
                .setPassword(encryptPwd)
                .setSalt(salt)
                .setName(name)
                .setEmail(email)
                .setPhone(phone)
                .setRole(role);

        int row = this.nodeUserDao.insertNodeUser(nodeUser);
        if (row != 1) {
            log.error("添加用户失败.");
            throw new InternalBusinessException("用户账号生成失败，请提交重试.");
        }
        log.info("成功添加一个用户");
        return new UserVO(account, name, phone, email, role);
    }

    /**
     * 查询所有的用户信息
     *
     * @param nodeId 当前联盟点编号
     * @return 用户信息封装视图对象列表
     */
    @Override
    public List<UserVO> queryAllUsers(Integer nodeId) {
        List<NodeUser> userList = nodeUserDao.queryAllByNodeId(nodeId);
        List<UserVO> userVOList = new ArrayList<>(userList.size());
        for (NodeUser nodeUser : userList) {
            UserVO userVO = new UserVO()
                    .setAccount(nodeUser.getAccount())
                    .setName(nodeUser.getName())
                    .setEmail(nodeUser.getEmail())
                    .setPhone(nodeUser.getPhone())
                    .setRole(nodeUser.getRole());
            userVOList.add(userVO);
        }
        return userVOList;
    }

    /**
     * 通过账号删除数据
     *
     * @param account 主键
     */
    @Override
    public void deleteByAccount(String account) {
        if (this.nodeUserDao.deleteByAccount(account) <= 0) {
            log.warn("删除用户失败");
            throw new InternalBusinessException("删除用户失败");
        }
    }

    /**
     * 重置用户密码为默认密码
     *
     * @param operator 操作者
     * @param account  用户账号
     */
    @Override
    public void updateUserPwdToDefault(String operator, String account) {
        NodeUser nodeUser = nodeUserDao.queryByAccount(account);
        String salt = JwtUtil.generateSalt();
        // 密码默认为手机号码后八位
        String password = nodeUser.getPhone().substring(3);
        String encryptPwd = new Sha256Hash(password, salt).toHex();
        nodeUser.setSalt(salt);
        nodeUser.setPassword(encryptPwd);
        nodeUserDao.updateNodeUserByAccount(nodeUser);
    }

    /**
     * 生成联盟点的用户账号
     * 生成策略：随机生成 + 查重
     * 账号规则：联盟点编号 + 两位数（随机生成）
     *
     * @param nodeId 联盟点编号
     * @return account
     */
    private String generateAccount(Integer nodeId) {
        // 默认生成的账号最多重复100次
        int generateTime = 100;
        String account = null;
        Set<String> accountSet = nodeUserDao.queryAllNodeAccountByNodeId(nodeId);
        for (int i = 0; i < generateTime; i++) {
            account = nodeId + IdUtil.generateRandomNumber(IdUtil.UID);
            // 如果set中没有该id则不重复，直接取用
            if (!accountSet.contains(account)) {
                break;
            }
        }
        return account;
    }


    /**
     * 更新用户的主要信息
     *
     * @param account 用户账号
     * @param name    姓名
     * @param email   邮箱
     * @param phone   手机号码
     * @param role    角色
     */
    @Override
    public void updateUserMainInfo(String account, String name, String email, String phone, String role) {
        NodeUser nodeUser = nodeUserDao.queryByAccount(account);
        nodeUser.setName(name).setEmail(email).setPhone(phone).setRole(role);
        try {
            nodeUserDao.updateNodeUserByAccount(nodeUser);
        } catch (Exception e) {
            log.info("更新用户主要信息失败.");
            throw new InternalBusinessException("更新用户信息失败.");
        }
        log.info("更新用户信息成功.");
    }
}
