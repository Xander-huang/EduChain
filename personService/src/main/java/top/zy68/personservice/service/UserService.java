package top.zy68.personservice.service;

import com.alibaba.fastjson.JSONObject;
import top.zy68.fbChainService.entity.PersonUser;
import top.zy68.personservice.exception.BaseException;
import top.zy68.personservice.pojo.UserInfoPo;
import top.zy68.personservice.pojo.UserParam;
import top.zy68.personservice.pojo.uservo.RspUserInfoVo;
import top.zy68.personservice.vo.UserInfoVO;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserService
 * @Description 用户服务
 * @Author Sustart
 * @Date2022/3/5 17:28
 * @Version 1.0
 **/
public interface UserService {
    /**
     * 生成账户Token
     *
     * @param personId 身份账号
     * @return token字符串
     */
    String generateAccountToken(String personId);

    /**
     * 删除登录信息（即删除登录salt）
     *
     * @param personId 身份证号
     */
    void deleteLoginInfo(String personId);

    /**
     * 通过personId获取其token加密的salt
     *
     * @param personId 身份证号
     * @return salt
     */
    String getTokenSaltByPersonId(String personId);

    /**
     * 更新用户信息：手机号码
     *
     * @param personId 身份证号
     * @param phone    新手机号码
     */
    void updateUserPhone(String personId, String phone);

    /**
     * 更新用户信息：邮箱
     *
     * @param personId 身份证号
     * @param email    新邮箱
     * @param captcha  验证码
     */
    void updateUserMail(String personId, String email, String captcha);

    /**
     * 更改用户密码
     *
     * @param personId  身份证号
     * @param originPwd 原密码
     * @param newPwd    新密码
     */
    String updateUserPwd(String personId, String originPwd, String newPwd, JSONObject oldPem);

    /**
     * 查询用户的基本信息
     *
     * @param personId 身份证号
     * @return 用户信息封装视图对象
     */
    UserInfoVO getUserInfo(String personId);

    /**
     * 通过personId查询用户信息
     *
     * @param personId 身份证号
     * @return 用户信息
     */
    PersonUser queryUserByPersonId(String personId);

    /**
     * 通过mail查询用户信息
     *
     * @param mail 邮箱账户
     * @return 用户信息
     */
    PersonUser queryUserByMail(String mail);

    /**
     * 重置用户密码
     *
     * @param mail    邮箱
     * @param newPwd  新密码
     * @param captcha 验证码
     */
    void resetUserPwd(String mail, String newPwd, String captcha);

    /**
     * 查询用户相关数据：教育经历数、成就数
     *
     * @param personId 身份证号
     * @return 两个数据
     */
    Map<String, Integer> getUserData(String personId);



    /**
     * add user by encrypt type
     * @param signUserId
     * @param appId
     * @param encryptType
     * @param privateKeyEncoded
     * @return
     * @throws Exception
     */
    UserInfoPo newUser(String signUserId, int encryptType,
                       String privateKeyEncoded,String password) throws Exception;



    PersonUser findBySignUserId(String signUserId) throws BaseException;

    PersonUser findByAddress(String address) throws BaseException;

    int countOfUser(UserParam param);

    List<RspUserInfoVo> findUserList(UserParam param);

    List<RspUserInfoVo> findUserListByAppId(UserParam param);


    void deleteBySignUserId(String signUserId) throws BaseException;


    Boolean deleteAllUserCache();


    Boolean deleteAllCredentialCache();
}
