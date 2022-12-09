package top.zy68.personservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import top.zy68.fbChainService.dao.AchieveDao;
import top.zy68.fbChainService.dao.EduRecordDao;
import top.zy68.fbChainService.dao.PersonDao;
import top.zy68.fbChainService.dao.PersonUserDao;
import top.zy68.fbChainService.entity.EduRecord;
import top.zy68.fbChainService.entity.Person;
import top.zy68.fbChainService.entity.PersonUser;
import top.zy68.personservice.enums.enums.CodeMessageEnums;
import top.zy68.personservice.enums.enums.KeyStatus;
import top.zy68.personservice.exception.BaseException;
import top.zy68.personservice.exception.ClientBusinessException;
import top.zy68.personservice.exception.InternalBusinessException;
import top.zy68.personservice.pojo.KeyStoreInfo;
import top.zy68.personservice.pojo.UserInfoPo;
import top.zy68.personservice.pojo.UserParam;
import top.zy68.personservice.pojo.uservo.RspUserInfoVo;
import top.zy68.personservice.service.IKeyStoreService;
import top.zy68.personservice.service.UserService;
import top.zy68.personservice.config.AesUtils;
import top.zy68.personservice.utils.JwtUtil;
import top.zy68.personservice.utils.CommonUtils;
import top.zy68.personservice.vo.UserInfoVO;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName UserServiceImpl
 * @Description 用户服务实现类
 * @Author Sustart
 * @Date2022/3/5 17:33
 * @Version 1.0
 **/
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    PersonUserDao personUserDao;
    @Resource
    PersonDao personDao;
    @Resource
    EduRecordDao eduRecordDao;
    @Resource
    AchieveDao achieveDao;

    @Resource
    IKeyStoreService keyStoreService;

    @Autowired
    private AesUtils aesUtils;

    @Qualifier(value = "cacheManager")
    private CacheManager cacheManager;

    /**
     * 生成账户Token
     *
     * @param personId 身份账号
     * @return token字符串
     */
    @Override
    public String generateAccountToken(String personId) {
        String salt = JwtUtil.generateSalt();
        // 用redis保存当前用户登录时的salt，退出登录时删除
        // 设计redisTokenKey拼接用于与修改邮箱等行为在redis保存验证码区别开
        String redisTokenKey = personId + "token";
        if (stringRedisTemplate.opsForValue().get(redisTokenKey) != null) {
            stringRedisTemplate.delete(redisTokenKey);
        }
        stringRedisTemplate.opsForValue().set(redisTokenKey, salt);
        // token有效期五天，并可根据情况更新
        long activeTime = 5 * 24 * 60 * 60L;
        String token = JwtUtil.sign(personId, salt, activeTime);
        if (Objects.isNull(token)) {
            throw new InternalBusinessException("Token生成失败");
        }
        return token;
    }

    /**
     * 通过personId获取其token加密的salt
     *
     * @param personId 身份证号
     * @return salt
     */
    @Override
    public String getTokenSaltByPersonId(String personId) {
        String redisTokenKey = personId + "token";
        String tokenSalt = stringRedisTemplate.opsForValue().get(redisTokenKey);
        if (Objects.isNull(tokenSalt)) {
            throw new ClientBusinessException("该用户没有登录salt");
        }
        return tokenSalt;
    }

    /**
     * 删除登录信息（即删除登录salt）
     *
     * @param personId 身份证号
     */
    @Override
    public void deleteLoginInfo(String personId) {
        // redis删除生成token时保存的salt
        String redisTokenKey = personId + "token";
        stringRedisTemplate.delete(redisTokenKey);
    }

    /**
     * 更新用户信息：手机号码
     *
     * @param personId 身份证号
     * @param phone    新手机号码
     */
    @Override
    public void updateUserPhone(String personId, String phone) {
        PersonUser personUser = personUserDao.queryPersonUserByPersonId(personId);
        if (personUser == null) {
            throw new ClientBusinessException("不存在该用户");
        }
        personUser.setPhone(phone);
        try {
            personUserDao.updatePersonUserByPersonId(personUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClientBusinessException("电话号码更改失败");
        }
        log.info("用户电话号码修改成功.");
    }

    /**
     * 更新用户信息：邮箱
     *
     * @param personId 身份证号
     * @param email    新邮箱
     * @param captcha  验证码
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void updateUserMail(String personId, String email, String captcha) {
        PersonUser personUser = personUserDao.queryPersonUserByPersonId(personId);
        if (personUser == null) {
            throw new ClientBusinessException("不存在该用户");
        }
        String cacheCaptcha = stringRedisTemplate.opsForValue().get(email);
        if (Objects.isNull(cacheCaptcha)) {
            throw new ClientBusinessException("验证码已过期");
        }
        if (!Objects.equals(cacheCaptcha, captcha)) {
            throw new ClientBusinessException("验证码错误");
        }

        stringRedisTemplate.delete(email);
        personUser.setEmail(email);

        try {
            personUserDao.updatePersonUserByPersonId(personUser);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("更改邮箱失败" + e.getMessage());
            throw new ClientBusinessException("更改邮箱失败.");
        }
    }

    /**
     * 更改用户密码
     *
     * @param personId  身份证号
     * @param originPwd 原密码
     * @param newPwd    新密码
     */
    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public String updateUserPwd(String personId, String originPwd, String newPwd, JSONObject oldPemFile) {
        PersonUser personUser = personUserDao.queryPersonUserByPersonId(personId);
        if (personUser == null) {
            throw new ClientBusinessException("不存在该用户");
        }

        // 校验原密码是否正确
        String originEncryptPwd = new Sha256Hash(originPwd, personUser.getSalt()).toHex();
        if (!Objects.equals(personUser.getPassword(), originEncryptPwd)) {
            log.info("修改密码失败，原密码不匹配.");
            throw new ClientBusinessException("更改失败，原密码不正确.");
        }
        String aPrivate = oldPemFile.getString("privateKey");
        String address = oldPemFile.getString("address");
        if(!Objects.equals(address,personUser.getAddress())){
            log.info("密钥不正确");
            throw new ClientBusinessException("更改失败，密钥不正确！");
        }


        // 存储新密码
        String salt = JwtUtil.generateSalt();
        String newEncryptPwd = new Sha256Hash(newPwd, salt).toHex();
        personUser.setPassword(newEncryptPwd);
        personUser.setSalt(salt);
        String newPri = "";
        try {
            personUserDao.updatePersonUserByPersonId(personUser);
            originPwd  = AesUtils.handPwdLength(originPwd);
            String oldPri = aesUtils.aesDecrypt(aPrivate, originPwd, "");
            newPwd =  AesUtils.handPwdLength(newPwd);
            newPri =  aesUtils.aesEncrypt(oldPri, newPwd, "");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("更改密码失败" + e.getMessage());
            throw new ClientBusinessException("更改失败");
        }
        log.info("成功更改用户密码.");
        return newPri;
    }

    /**
     * 重置用户密码
     *
     * @param mail    邮箱
     * @param newPwd  新密码
     * @param captcha 验证码
     */
    @Override
    public void resetUserPwd(String mail, String newPwd, String captcha) {
        PersonUser personUser = personUserDao.queryPersonUserByEmail(mail);
        if (personUser == null) {
            throw new ClientBusinessException("不存在该用户");
        }
        String cacheCaptcha = stringRedisTemplate.opsForValue().get(mail);
        if (cacheCaptcha == null || !Objects.equals(cacheCaptcha, captcha)) {
            throw new ClientBusinessException("验证码不存在或已过期.");
        }
        // 存储新密码
        String salt = JwtUtil.generateSalt();
        String newEncryptPwd = new Sha256Hash(newPwd, salt).toHex();
        personUser.setPassword(newEncryptPwd);
        personUser.setSalt(salt);
        stringRedisTemplate.delete(mail);
        int row = personUserDao.updatePersonUserByPersonId(personUser);
        if (row != 1) {
            log.info("更改用户信息失败.");
            throw new ClientBusinessException("更改失败");
        }
        log.info("成功更改用户密码.");
    }

    /**
     * 查询用户的基本信息
     *
     * @param personId 身份证号
     * @return 用户信息封装视图对象
     */
    @Override
    public UserInfoVO getUserInfo(String personId) {
        Person person = personDao.queryPersonByPersonId(personId);
        PersonUser personUser = personUserDao.queryPersonUserByPersonId(personId);
        if (person == null || personUser == null) {
            throw new ClientBusinessException("不存在该用户");
        }
        return new UserInfoVO()
                .setPersonId(person.getPersonId())
                .setName(person.getName())
                .setGender(person.getGender())
                .setNation(person.getNation())
                .setBirthday(person.getBirthday())
                .setEmail(personUser.getEmail())
                .setPhone(personUser.getPhone())
                .setAddress(person.getAddress());
    }

    /**
     * 通过personId查询用户信息
     *
     * @param personId 身份证号
     * @return 用户信息
     */
    @Override
    public PersonUser queryUserByPersonId(String personId) {
        return personUserDao.queryPersonUserByPersonId(personId);
    }

    /**
     * 通过mail查询用户信息
     *
     * @param mail 邮箱账户
     * @return 用户信息
     */
    @Override
    public PersonUser queryUserByMail(String mail) {
        return personUserDao.queryPersonUserByEmail(mail);
    }

    /**
     * 查询用户相关数据：教育经历数、成就数
     *
     * @param personId 身份证号
     * @return 两个数据
     */
    @Override
    public Map<String, Integer> getUserData(String personId) {
        Map<String, Integer> resMap = new HashMap<>(2);
        List<EduRecord> eduRecords = eduRecordDao.queryAllByPersonId(personId);

        int achieveNum = 0;
        for (EduRecord eduRecord : eduRecords) {
            achieveNum += achieveDao.countEduAchieve(eduRecord.getEduId());
        }

        resMap.put("education", eduRecords.size());
        resMap.put("achieve", achieveNum);

        return resMap;
    }

    /**
     * add user by encrypt type
     */
    @Override
    public UserInfoPo newUser(String signUserId, int encryptType,
                                 String privateKeyEncoded,String password) throws Exception {
        log.info("start addUser signUserId:{},appId:{},encryptType:{}", signUserId,
                encryptType);
        // check user uuid exist
        PersonUser checkSignUserIdExists =personUserDao.findUserBySignUserId(signUserId);
        if (Objects.nonNull(checkSignUserIdExists)) {

            throw new BaseException(CodeMessageEnums.USER_EXISTS);

        }

        // get keyStoreInfo
        KeyStoreInfo keyStoreInfo;
        if (StringUtils.isNotBlank(privateKeyEncoded)) {
            String privateKey;
            // decode base64 as raw private key
            try {
                privateKey = new String(CommonUtils.base64Decode(privateKeyEncoded));
                keyStoreInfo = keyStoreService.getKeyStoreFromPrivateKey(privateKey, encryptType,password);
            } catch (Exception ex) {
                log.error("newUser privatekey encoded format error：{}", privateKeyEncoded);
                throw new BaseException(CodeMessageEnums.PRIVATE_KEY_DECODE_FAIL);
            }
        } else {
            keyStoreInfo = keyStoreService.newKeyByType(encryptType,password);
        }

        // save user.
        UserInfoPo userInfoPo = new UserInfoPo();
        BeanUtils.copyProperties(keyStoreInfo, userInfoPo);
        userInfoPo.setEncryptType(encryptType);
        userInfoPo.setSignUserId(signUserId);
        //userInfoPo.setAppId(appId);
        log.info("end escda");
        return userInfoPo;
    }


    /**
     * save user.
     */
    public RspUserInfoVo saveUser(UserInfoPo userInfoPo) {
        // save user
        //userDao.insertUserInfo(userInfoPo);

        // return
        RspUserInfoVo rspUserInfoVo = new RspUserInfoVo();
        BeanUtils.copyProperties(userInfoPo, rspUserInfoVo);

        return rspUserInfoVo;
    }

    /**
     * query user by userId.
     */
    @Cacheable(cacheNames = "user")
    public PersonUser findBySignUserId(String signUserId) throws BaseException {
        log.info("start findBySignUserId. signUserId:{}", signUserId);
        PersonUser user=null; // userDao.findUserBySignUserId(signUserId);
        if (Objects.isNull(user) ) {
            log.warn("fail findBySignUserId, user not exists. userId:{}", signUserId);
            throw new BaseException(CodeMessageEnums.USER_NOT_EXISTS);
        }
        Optional.ofNullable(user)
                .ifPresent(u -> u.setPrivateKey(aesUtils.aesDecrypt(u.getPrivateKey())));
        log.info("end findBySignUserId. userId:{}", signUserId);
        return user;
    }

    public PersonUser findByAddress(String address) throws BaseException {
        log.info("start findUserByAddress. address:{}", address);
        PersonUser user =null; // userDao.findUserByAddress(address);
        if (Objects.isNull(user)) {
            log.warn("fail findUserByAddress, user not exists. address:{}", address);
            throw new BaseException(CodeMessageEnums.USER_NOT_EXISTS);
        }
        Optional.ofNullable(user)
                .ifPresent(u -> u.setPrivateKey(aesUtils.aesDecrypt(u.getPrivateKey())));
        log.info("end findUserByAddress. address:{}", address);
        return user;
    }

    /**
     * count of user.
     */
    public int countOfUser(UserParam param) {
        Integer count =null; //userDao.countOfUser(param);
        return count == null ? 0 : count;
    }

    /**
     * query user list.
     *
     * @param param encryptType 1: guomi, 0: standard
     */
    public List<RspUserInfoVo> findUserList(UserParam param) {
        log.info("start findUserList.");
        List<UserInfoPo> users =null; // userDao.findUserList(param);
        List<RspUserInfoVo> rspUserInfoVos = new ArrayList<>();
        for (UserInfoPo user : users) {
            RspUserInfoVo rspUserInfoVo = new RspUserInfoVo();
            BeanUtils.copyProperties(user, rspUserInfoVo);
            rspUserInfoVo.setPrivateKey(aesUtils.aesDecrypt(user.getPrivateKey()));
            rspUserInfoVos.add(rspUserInfoVo);
        }
        return rspUserInfoVos;
    }

    public List<RspUserInfoVo> findUserListByAppId(UserParam param) {
        log.info("start findUserListByAppId.");
        List<UserInfoPo> users =null; // userDao.findUserListByAppId(param);
        List<RspUserInfoVo> rspUserInfoVos = new ArrayList<>();
        for (UserInfoPo user : users) {
            RspUserInfoVo rspUserInfoVo = new RspUserInfoVo();
            BeanUtils.copyProperties(user, rspUserInfoVo);
            rspUserInfoVos.add(rspUserInfoVo);
        }
        return rspUserInfoVos;
    }



    /**
     * delete user by signUserId
     */
    @CacheEvict(cacheNames = "user", beforeInvocation = true)
    public void deleteBySignUserId(String signUserId) throws BaseException {
        log.info("start deleteByUuid signUserId:{}", signUserId);
        UserInfoPo user =null; // userDao.findUserBySignUserId(signUserId);
        if (Objects.isNull(user) || user.getStatus().equals(KeyStatus.SUSPENDED.getValue())) {
            log.warn("fail deleteByUuid, user not exists. signUserId:{}", signUserId);
            throw new BaseException(CodeMessageEnums.USER_NOT_EXISTS);
        }
        //userDao.deleteUserBySignUserId(signUserId);
        log.info("end deleteByUuid.");
    }


    public Boolean deleteAllUserCache() {
        log.info("delete all user cache");

        Cache cache = cacheManager.getCache("user");
        if (cache != null) {
            cache.clear();
        }
        return true;
    }

    public Boolean deleteAllCredentialCache() {
        log.info("delete all Credential cache");

        Cache cache = cacheManager.getCache("getCredentials");
        if (cache != null) {
            cache.clear();
        }
        return true;
    }

    public UserInfoPo findLatestUpdatedUser() {
        UserInfoPo user =null; // userDao.findLatestUpdateUser();
        return user;
    }

}
