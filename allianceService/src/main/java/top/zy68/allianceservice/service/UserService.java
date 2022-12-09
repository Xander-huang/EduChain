package top.zy68.allianceservice.service;

import top.zy68.allianceservice.pojo.vo.UserInfoVO;
import top.zy68.fbChainService.entity.NodeUser;

/**
 * @ClassName UserService
 * @Description 当前登录用户管理相关
 * @Author Sustart
 * @Date2022/3/16 12:26
 * @Version 1.0
 **/
public interface UserService {
    /**
     * 生成账户Token
     *
     * @param account 账号
     * @return token字符串
     */
    String generateAccountToken(String account);

    /**
     * 通过邮箱查询用户信息
     *
     * @param email 邮箱
     * @return 用户信息封装视图对象
     */
    NodeUser queryUserByMail(String email);

    /**
     * 查询用户信息
     *
     * @param account 当前登录用户
     * @return 用户信息封装视图对象
     */
    UserInfoVO queryUserInfo(String account);

    /**
     * 更新用户手机号码
     *
     * @param account account
     * @param phone   phone
     */
    void updateUserPhone(String account, String phone);

    /**
     * 更新用户密码
     *
     * @param account   账号
     * @param originPwd 原密码
     * @param newPwd    新密码
     */
    void updateUserPwd(String account, String originPwd, String newPwd);

    /**
     * 通过账号查询用户
     *
     * @param account 用户账号
     * @return 联盟点用户
     */
    NodeUser queryUserByAccount(String account);

    /**
     * 获取Token对应的盐
     *
     * @param account 账号
     * @return 盐
     */
    String getTokenSaltByAccount(String account);

    /**
     * 删除登录信息（即删除登录salt）
     *
     * @param account 用户账号
     */
    void deleteLoginInfo(String account);
}
