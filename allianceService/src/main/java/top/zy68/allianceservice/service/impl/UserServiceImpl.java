package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.exception.InternalBusinessException;
import top.zy68.allianceservice.pojo.vo.UserInfoVO;
import top.zy68.allianceservice.service.UserService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.allianceservice.util.JwtUtil;
import top.zy68.fbChainService.dao.AllianceNodeDao;
import top.zy68.fbChainService.dao.NodeUserDao;
import top.zy68.fbChainService.entity.AllianceNode;
import top.zy68.fbChainService.entity.NodeUser;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserServiceImpl
 * @Description 用户管理相关
 * @Author Sustart
 * @Date2022/3/16 12:27
 * @Version 1.0
 **/
@Slf4j
@Service("UserService")
public class UserServiceImpl implements UserService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    AllianceNodeDao allianceNodeDao;
    @Resource
    private NodeUserDao nodeUserDao;

    /**
     * 生成账户Token
     *
     * @param account 账号
     * @return token字符串
     */
    @Override
    public String generateAccountToken(String account) {
        // token有效期为5天
        long activeTime = 5 * 24 * 60 * 60L;
        String redisTokenKey = account + "token";
        String salt = JwtUtil.generateSalt();
        String token = JwtUtil.sign(account, salt, activeTime);
        // 如果已经处于登录状态，把原来的salt删掉
        if (stringRedisTemplate.opsForValue().get(redisTokenKey) != null) {
            stringRedisTemplate.delete(redisTokenKey);
        }
        if (token == null) {
            log.info("用户token生成失败.");
            throw new InternalBusinessException("生成token失败");
        }
        // 登录salt有效时间与jwt生成token的有效时间保持一致
        stringRedisTemplate.opsForValue().set(redisTokenKey, salt, activeTime, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 查询用户信息
     *
     * @param account 当前登录用户
     * @return 用户信息封装视图对象
     */
    @Override
    public UserInfoVO queryUserInfo(String account) {
        Integer nodeNumber = IdUtil.getNodeId(account);
        AllianceNode allianceNode = allianceNodeDao.queryByNodeId(nodeNumber);
        if (ObjectUtils.isEmpty(allianceNode)) {
            log.info("不存在该联盟点.");
            throw new ClientBusinessException("不存在该联盟点.");
        }
        NodeUser nodeUser = nodeUserDao.queryByAccount(account);
        if (ObjectUtils.isEmpty(nodeUser)) {
            log.info("不存在该用户.");
            throw new ClientBusinessException("不存在该用户.");
        }
        return new UserInfoVO()
                .setType(allianceNode.getType())
                .setNodeName(allianceNode.getName())
                .setName(nodeUser.getName())
                .setPhone(nodeUser.getPhone())
                .setEmail(nodeUser.getEmail())
                .setRole(nodeUser.getRole());
    }

    /**
     * 更新用户手机号码
     *
     * @param account account
     * @param phone   phone
     */
    @Override
    public void updateUserPhone(String account, String phone) {
        NodeUser currUser = nodeUserDao.queryByAccount(account);
        currUser.setPhone(phone);
        int row = nodeUserDao.updateNodeUserByAccount(currUser);
        if (row != 1) {
            log.info("用户更改手机号码失败");
            throw new InternalBusinessException("用户更改手机号码失败");
        }
        log.info("用户更改手机号码成功");
    }

    /**
     * 更新用户密码
     *
     * @param account   账号
     * @param originPwd 原密码
     * @param newPwd    新密码
     */
    @Override
    public void updateUserPwd(String account, String originPwd, String newPwd) {
        NodeUser nodeUser = nodeUserDao.queryByAccount(account);
        if (nodeUser == null) {
            throw new ClientBusinessException("不存在该用户");
        }

        // 校验原密码是否正确
        String originEncryptPwd = new Sha256Hash(originPwd, nodeUser.getSalt()).toHex();
        if (!Objects.equals(nodeUser.getPassword(), originEncryptPwd)) {
            log.info("修改密码失败，原密码不匹配.");
            throw new ClientBusinessException("更改失败，原密码不正确.");
        }

        // 存储新密码
        String salt = JwtUtil.generateSalt();
        String newEncryptPwd = new Sha256Hash(newPwd, salt).toHex();
        nodeUser.setPassword(newEncryptPwd);
        nodeUser.setSalt(salt);

        int row = nodeUserDao.updateNodeUserByAccount(nodeUser);
        if (row != 1) {
            log.info("更改用户信息失败.");
            throw new ClientBusinessException("更改失败");
        }
        log.info("成功更改用户密码.");
    }

    /**
     * 通过邮箱查询用户信息
     *
     * @param email 邮箱
     * @return 用户信息封装视图对象
     */
    @Override
    public NodeUser queryUserByMail(String email) {
        return nodeUserDao.queryUserByMail(email);
    }

    /**
     * 通过账号查询用户
     *
     * @param account 用户账号
     * @return 联盟点用户
     */
    @Override
    public NodeUser queryUserByAccount(String account) {
        return nodeUserDao.queryByAccount(account);
    }

    /**
     * 获取Token对应的盐
     *
     * @param account 账号
     * @return 盐
     */
    @Override
    public String getTokenSaltByAccount(String account) {
        String redisTokenKey = account + "token";
        return stringRedisTemplate.opsForValue().get(redisTokenKey);
    }

    /**
     * 删除登录信息（即删除登录salt）
     *
     * @param account 用户账号
     */
    @Override
    public void deleteLoginInfo(String account) {
        String redisTokenKey = account + "token";
        stringRedisTemplate.delete(redisTokenKey);
    }
}
