package top.zy68.personservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import top.zy68.fbChainService.service.IFileService;
import top.zy68.personservice.api.ResponseBean;
import top.zy68.personservice.api.ResultCode;
import top.zy68.personservice.config.ConstantProperties;
import top.zy68.personservice.enums.enums.EncryptTypes;
import top.zy68.personservice.exception.BaseException;
import top.zy68.personservice.pojo.MailDO;
import top.zy68.fbChainService.service.KeyService;
import top.zy68.fbChainService.dao.PersonDao;
import top.zy68.fbChainService.dao.PersonUserDao;
import top.zy68.fbChainService.entity.Person;
import top.zy68.fbChainService.entity.PersonUser;
import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;
import top.zy68.fbChainService.service.PersonSolService;
import top.zy68.personservice.dto.RegisterDTO;
import top.zy68.personservice.exception.ClientBusinessException;
import top.zy68.personservice.exception.InternalBusinessException;
import top.zy68.personservice.pojo.UserInfoPo;
import top.zy68.personservice.pojo.uservo.RspUserInfoVo;
import top.zy68.personservice.service.LoginService;
import top.zy68.personservice.service.MailDOService;
import top.zy68.personservice.service.UserService;
import top.zy68.personservice.utils.CommonUtils;
import top.zy68.personservice.utils.DateUtil;
import top.zy68.personservice.utils.JwtUtil;
import top.zy68.personservice.utils.ValidateCodeUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static top.zy68.personservice.enums.enums.CodeMessageEnums.*;
import static top.zy68.personservice.enums.enums.CodeMessageEnums.PRIVATEKEY_NOT_SUPPORT_TRANSFER;

/**
 * @ClassName LoginServiceImpl
 * @Description 登录服务实现类
 * @Author Sustart
 * @Date2022/3/7 11:39
 * @Version 1.0
 **/
@Service("LoginService")
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    PersonUserDao personUserDao;
    @Resource
    PersonDao personDao;
    @Resource
    MailDOService mailDOService;
    @Resource
    MailDO mailDO;
    @Resource
    PersonSolService personSolService;
    @Resource
    KeyService keyService;

    @Resource
    ConstantProperties properties;

    @Resource
    UserService userService;

    @Resource
    IFileService fileService;


    /**
     * 注册服务
     *
     * @param registerDTO 注册信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterDTO registerDTO, HttpServletResponse response) throws Exception{

        try {
            String mailCaptcha = stringRedisTemplate.opsForValue().get(registerDTO.getEmail());
            if (mailCaptcha == null || mailCaptcha.length() == 0 || !Objects.equals(mailCaptcha, registerDTO.getCaptcha())) {
                log.warn("注册用户的验证码不存在或已过期");
                throw new ClientBusinessException("验证码不存在或已过期");
            }

            Person person1 = personDao.queryPersonByPersonId(registerDTO.getPersonId());

            if(Objects.nonNull(person1)){
                throw new ClientBusinessException("该用户已经存在！");
            }

            String salt = JwtUtil.generateSalt();
            String encryptPwd = new Sha256Hash(registerDTO.getPassword(), salt).toHex();
            Person person = new Person()
                    .setPersonId(registerDTO.getPersonId())
                    .setName(registerDTO.getName())
                    .setNation(registerDTO.getNation())
                    .setGender(registerDTO.getGender())
                    .setBirthday(registerDTO.getBirthday())
                    .setAddress(registerDTO.getAddress());
            PersonUser personUser = new PersonUser()
                    .setEmail(registerDTO.getEmail())
                    .setPhone(registerDTO.getPhone())
                    .setPersonId(registerDTO.getPersonId())
                    .setPassword(encryptPwd)
                    .setType(registerDTO.getType())
                    .setSalt(salt);

            // 上链
            PersonMainInfo personMainInfo = new PersonMainInfo(person.getPersonId(), person.getName(), person.getGender(), person.getNation(), DateUtil.dateToString(person.getBirthday(), DateUtil.PATTERN_DATE));
            String transactionHash = personSolService.insert(personMainInfo);
            if (Objects.isNull(transactionHash)) {
                log.warn("个人信息上链失败.");
                throw new ClientBusinessException("个人信息上链失败.");
            }
            log.info("{} 的个人信息上链成功，交易哈希为：{}", person.getPersonId(), transactionHash);
            person.setTransactionHash(transactionHash);

            String signUserId = registerDTO.getPersonId();

            //boolean returnPrivateKey = registerDTO.isReturnPrivateKey();
            // validate signUserId
            if (StringUtils.isEmpty(signUserId)) {
                throw new BaseException(PARAM_SIGN_USER_ID_IS_BLANK);
            }
            if (!CommonUtils.isLetterDigit(signUserId)
                    || !CommonUtils.checkLengthWithin_64(signUserId)) {
                throw new BaseException(PARAM_SIGN_USER_ID_IS_INVALID);
            }
            /*if (StringUtils.isBlank(appId)) {
                throw new BaseException(PARAM_APP_ID_IS_BLANK);
            }
            if (!CommonUtils.isLetterDigit(appId) || !CommonUtils.checkLengthWithin_64(appId)) {
                throw new BaseException(PARAM_APP_ID_IS_INVALID);
            }*/
            /*if (encryptType != EncryptTypes.STANDARD.getValue()
                    && encryptType != EncryptTypes.GUOMI.getValue()) {
                throw new BaseException(PARAM_ENCRYPT_TYPE_IS_INVALID);
            }
            if (returnPrivateKey == true && !properties.isSupportPrivateKeyTransfer()) {
                throw new BaseException(PRIVATEKEY_NOT_SUPPORT_TRANSFER);
            }*/
            //产生公私钥+address
            String password = registerDTO.getPassword();
            UserInfoPo userInfo = userService.newUser(signUserId, 0, null,password);

            /*if (returnPrivateKey == false) {
                userInfo.setPrivateKey("");
            }*/
            //todo 下载代理重加密的公私钥
            //
            String reProxyPri = keyService.generateOneKey(personUser.getPersonId(), 0);

            personDao.insertPerson(person);
            personUser.setAddress(userInfo.getAddress());
            personUser.setPublicKey(userInfo.getPublicKey());
            personUser.setSignUserId(person.getName());
            personUserDao.insertPersonUser(personUser);
            stringRedisTemplate.delete(registerDTO.getEmail());
            log.info("用户：{}，注册成功", registerDTO.getPersonId());
            String pri = userInfo.getPrivateKey();
            String ecdsaPriPem = covertStrToPem(pri);
            String reProxyPriPem = covertStrToPem(reProxyPri);


            List<byte[]> objects = new ArrayList<>();
            objects.add(ecdsaPriPem.getBytes());
            objects.add(reProxyPriPem.getBytes());
            fileService.downZipFile(objects,response);
            return ;

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;

        }

    }



    public String covertStrToPem(String pri){
        String begin = "-----BEGIN PRIVATE KEY-----\n";

        String end = "\n-----END PRIVATE KEY-----";

        return  begin + pri  + end;
    }

    /**
     * 给指定邮箱发送验证邮箱的邮件
     *
     * @param email 邮箱
     */
    @Override
    public void sendEmailVerifyCode(String email) {
        // 获取验证码
        String captcha = ValidateCodeUtil.getCaptcha();
        String subject = "终身教育服务平台邮箱验证";
        String text = "尊敬的先生/女士，您好：\n 您的邮箱验证码是：" + captcha + "，有效时间为 5 分钟，请尽快处理。\n 如非本人操作，请忽略！";

        mailDO.setTo(email);
        mailDO.setSubject(subject);
        mailDO.setText(text);
        mailDOService.sendMail(mailDO);

        // 发送失败
        if (!mailDO.getStatus()) {
            log.warn("Failed to send a mail to : " + email);
            throw new RuntimeException(mailDO.getError());
        }
        log.info("Succeed to send a mail to : " + email);

        // redis保存验证码用于校验，五分钟内有效。
        stringRedisTemplate.opsForValue().set(email, captcha, 5, TimeUnit.MINUTES);
    }

    /**
     * 获取登录时的图片验证码
     *
     * @return 包含图片的Base64编码和验证码字符串
     */
    @Override
    public Map<String, String> getLoginCaptcha() {
        String captcha = null;
        String image = null;

        try {
            captcha = ValidateCodeUtil.getCaptcha();
            image = ValidateCodeUtil.getCaptchaImg(captcha);
        } catch (Exception e) {
            log.error("生成验证码图片异常！");
            throw new InternalBusinessException("生成图片验证码失败.");
        }

        Map<String, String> resData = new HashMap<>(2);
        resData.put("img", image);
        resData.put("imgKey", captcha);
        log.info("成功发送验证码图片.");
        return resData;
    }
}
