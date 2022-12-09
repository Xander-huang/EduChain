package top.zy68.allianceservice.service;

import top.zy68.allianceservice.pojo.vo.UserVO;

import java.util.List;

/**
 * @ClassName ManageUserService
 * @Description 顶级管理员管理用户的service
 * @Author Sustart
 * @Date2022/3/19 21:15
 * @Version 1.0
 **/
public interface ManageUserService {

    /**
     * 查询所有用户信息
     *
     * @param nodeNumber 当前联盟点编号
     * @return 用户信息封装视图对象列表
     */
    List<UserVO> queryAllUsers(Integer nodeNumber);

    /**
     * 添加一个用户
     *
     * @param name     姓名
     * @param email    邮箱
     * @param phone    手机号码
     * @param operator 操作者
     * @param role     角色
     */
    UserVO insertUser(String operator, String name, String email, String phone, String role);

    /**
     * 通过账号删除账户
     *
     * @param account 账号
     */
    void deleteByAccount(String account);

    /**
     * 重置用户密码为默认密码
     *
     * @param operator 操作者
     * @param account  用户账号
     */
    void updateUserPwdToDefault(String operator, String account);

    /**
     * 更新用户的主要信息
     *
     * @param account 用户账号
     * @param name    姓名
     * @param email   邮箱
     * @param phone   手机号码
     * @param role    角色
     */
    void updateUserMainInfo(String account, String name, String email, String phone, String role);
}
