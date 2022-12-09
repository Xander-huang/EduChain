package top.zy68.personservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import top.zy68.fbChainService.dao.*;
import top.zy68.fbChainService.entity.*;
import top.zy68.fbChainService.grpc.recrypt.ReCrypt;
import top.zy68.fbChainService.grpc.recrypt.ReCryptGrpc;
import top.zy68.fbChainService.model.chainMainInfo.AchieveMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.BlockMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.CourseMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;
import top.zy68.fbChainService.service.*;
import top.zy68.personservice.dto.AllianceDTO;
import top.zy68.personservice.exception.ClientBusinessException;
import top.zy68.personservice.exception.NormalBusinessException;
import top.zy68.personservice.pojo.*;
import top.zy68.personservice.service.EduService;
import top.zy68.personservice.service.MailDOService;
import top.zy68.personservice.vo.ChainBasicVO;
import top.zy68.personservice.vo.ChainDataVO;
import top.zy68.personservice.vo.ChainEducationVO;
import top.zy68.personservice.vo.EduRecordScoreVO;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static top.zy68.fbChainService.common.ApiConst.GrpcConst.host;
import static top.zy68.fbChainService.common.ApiConst.GrpcConst.serverPort;


/**
 * 教育记录(EducationRecord)表服务实现类
 *
 * @author Sustart
 * @since 2022-02-16 19:30:00
 */
@Service("eduService")
@Slf4j
public class EduServiceImpl implements EduService {


    @Resource
    EduRecordDao eduRecordDao;
    @Resource
    CourseScoreDao courseScoreDao;
    @Resource
    CourseDao courseDao;
    @Resource
    PersonDao personDao;
    @Resource
    PersonUserDao personUserDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Qualifier("bytesRedisTemplate")
    RedisTemplate<String, byte[]> bytesRedisTemplate;
    @Resource
    MailDOService mailDOService;
    @Resource
    MailDO mailDO;
    @Resource
    PersonSolService personSolService;
    @Resource
    EduRecordSolService eduRecordSolService;
    @Resource
    CourseSolService courseSolService;
    @Resource
    AchieveSolService achieveSolService;
    @Resource
    ClientService clientService;
    @Resource
    UserCaptchaDao userCaptchaDao;
    @Resource
    KeyDao keyDao;


    /**
     * 通过身份证号查询用户的所有教育记录（含课程）
     *
     * @param personId 身份证号
     * @return 所有教育记录（含课程）
     */
    @Override
    public List<EduRecordScoreVO> queryEduRecord(String personId) {
        // 查询所有的教育经历
        List<EduRecord> records = eduRecordDao.queryAllByPersonId(personId);
        if (ObjectUtils.isEmpty(records)) {
            throw new NormalBusinessException("该用户暂不存在教育经历.");
        }

        List<EduRecordScoreVO> resData = new ArrayList<>(records.size());
        for (EduRecord e : records) {
            //查询该教育经历下的所有课程
            List<CourseScore> eduCourseList = courseScoreDao.queryAllByEduId(e.getEduId());
            if (ObjectUtils.isEmpty(eduCourseList)) {
                // 该经历下还未有课程成绩
                continue;
            }
            List<CourseBO> courseBOList = new ArrayList<>(eduCourseList.size());
            EduRecordScoreVO eduRecordScoreVO = new EduRecordScoreVO()
                    .setEduId(e.getEduId())
                    .setType(e.getType())
                    .setBeginTime(e.getBeginTime())
                    .setEndTime(e.getEndTime())
                    .setAcquireCredit(e.getAcquireCredit())
                    .setGraduateCredit(e.getGraduateCredit())
                    .setNodeName(e.getNodeName())
                    .setMajorName(e.getMajorName());
            for (CourseScore p : eduCourseList) {
                Course course = courseDao.queryByCourseId(p.getCourseId());
                if (ObjectUtils.isEmpty(course)) {
                    // 该课号课程不存在
                    continue;
                }
                courseBOList.add(new CourseBO()
                        .setCourseId(p.getCourseId())
                        .setName(course.getName())
                        .setCredit(course.getCredit())
                        .setCertifyUri(p.getCertifyUri())
                );
            }
            eduRecordScoreVO.setCourse(courseBOList);
            resData.add(eduRecordScoreVO);
        }
        return resData;
    }

    /**
     * 通过身份账号查询该用户的所有教育记录
     *
     * @param personId 身份证号
     * @return 多条教育记录
     */
    @Override
    public List<AllianceDTO> queryAllAlliance(String personId) {
        List<EduRecord> records = eduRecordDao.queryAllByPersonId(personId);
        if (ObjectUtils.isEmpty(records)) {
            throw new NormalBusinessException("该用户尚未参与机构学习");
        }

        List<AllianceDTO> alliances = new LinkedList<>();
        for (EduRecord record : records) {
            if (ObjectUtils.isEmpty(record.getEduTransactionHash())) {
                AllianceDTO allianceDTO = new AllianceDTO(record.getEduId(), record.getNodeName(), record.getType(), record.getMajorName());
                alliances.add(allianceDTO);
            }
        }
        return alliances;
    }

    /**
     * 分享已经上链的数据
     * 将访问key/数据验证码发送到邮箱
     *
     * @param personId  身份证号
     * @param eduIdList 教育经历ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void shareDataInChain(String personId, List<String> eduIdList) {
        Person person = personDao.queryPersonByPersonId(personId);
        PersonUser user = personUserDao.queryPersonUserByPersonId(personId);
        // 封装为：经历ID;经历交易哈希;成就交易哈希;课程交易哈希;
        List<String> eduList = new ArrayList<>(eduIdList.size());
        ObjectMapper jsonUtil = new ObjectMapper();

        for (String eduId : eduIdList) {
            EduRecord eduRecord = eduRecordDao.queryByEduId(eduId);
            StringBuilder sb = new StringBuilder();

            if (ObjectUtils.isEmpty(eduRecord)) {
                throw new ClientBusinessException(eduId + " 教育经历不存在.");
            }
            if (ObjectUtils.isEmpty(eduRecord.getEduTransactionHash())) {
                throw new ClientBusinessException(eduRecord.getNodeName() + " - " + eduRecord.getType() + " - " + eduRecord.getMajorName() + "的经历还未上链，不可分享.");
            }

            sb.append(eduId).append(";");
            sb.append(eduRecord.getEduTransactionHash()).append(";");
            // 校验端暂时不需要通过成就交易哈希和课程交易哈希查看区块信息，

            eduList.add(sb.toString());
        }

        String accessKey = UUID.randomUUID().toString().replaceAll("-", "");

        try {
            String shareDataJson = jsonUtil.writeValueAsString(new ShareDataBO(personId, person.getTransactionHash(), eduList));

            //调用grpc进行加密
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, serverPort)
                    .usePlaintext().build();
            ReCryptGrpc.reCryptBlockingStub reCryptBlockingStub = ReCryptGrpc.newBlockingStub(channel);

            String pubKey = keyDao.selectPubKeyById(user.getPersonId());

            ReCrypt.ACryptRequest cryptRequest = ReCrypt.ACryptRequest.newBuilder()
                    .setAPublicKey(pubKey).setPlainText(shareDataJson).build();

            ReCrypt.ACryptResponse aCryptResponse = reCryptBlockingStub.cryptText(cryptRequest);
            String capsule = aCryptResponse.getCapsule();
            ByteString ciphereText = aCryptResponse.getCiphereText();
            int code = aCryptResponse.getCode();
            String msg = aCryptResponse.getMsg();
            //todo 校验code，存参数
            byte[] bytes = ciphereText.toByteArray();
            bytesRedisTemplate.opsForValue().set(accessKey,bytes);
            userCaptchaDao.insertOneCaptcha(user.getPersonId(), accessKey, aCryptResponse.getCapsule(),bytes);
            channel.isShutdown();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //发送邮件
        sendEmailAccessKey(user.getEmail(), accessKey);


    }


    /**
     * 给指定邮箱发送数据校验码
     *
     * @param email     邮箱
     * @param accessKey 数据校验码
     */
    @Override
    public void sendEmailAccessKey(String email, String accessKey) {
        String subject = "终身教育服务平台-数据校验";
        String text = "亲爱的同学，您好：\n    您的数据校验码为：   "
                + accessKey
                + "\n通过该校验码可跳转至：website.com 查看您的身份信息和已选择分享的教育经历。\n"
                + "注意：通过此验证码可查看您的敏感信息，请避免泄漏!";

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
    }

    /**
     * 查询链上数据
     *
     * @param personId 身份证号
     * @return 封装数据
     */
    @Override
    public ChainDataVO getChainData(String personId) {
        Person person = personDao.queryPersonByPersonId(personId);
        List<EduRecord> eduRecords = eduRecordDao.queryAllByPersonId(personId);
        List<String> eduList = new LinkedList<>();

        PersonMainInfo personMainInfo = personSolService.select(personId);
        BlockMainInfo blockMainInfo = clientService.getBlockInfoByTransactionHash(person.getTransactionHash());

        if (ObjectUtils.isEmpty(eduRecords)) {
            return new ChainDataVO(new ChainBasicVO(personMainInfo, blockMainInfo), null);
        }

        for (EduRecord eduRecord : eduRecords) {

            StringBuilder sb = new StringBuilder();
            if (ObjectUtils.isEmpty(eduRecord.getEduTransactionHash())) {
                continue;
            }
            sb.append(eduRecord.getEduId()).append(";");
            sb.append(eduRecord.getEduTransactionHash()).append(";");
            // 校验端暂时不需要通过成就交易哈希和课程交易哈希查看区块信息
            eduList.add(sb.toString());
        }

        List<ChainEducationVO> chainEducationVOList = getEducationVOList(eduList);

        return new ChainDataVO(new ChainBasicVO(personMainInfo, blockMainInfo), chainEducationVOList);
    }




    /**
     * 封装教育数据视图列表
     *
     * @param eduList 教育数据key列表
     * @return 封装的教育视图列表
     */
    private List<ChainEducationVO> getEducationVOList(List<String> eduList) {
        if (ObjectUtils.isEmpty(eduList)) {
            return null;
        }
        List<ChainEducationVO> resList = new ArrayList<>(eduList.size());
        for (String eduKey : eduList) {
            String[] keys = eduKey.split(";");
            // 保证传入的数据列表：教育经历ID，教育经历交易hash，课程数据交易hash，成就数据交易hash
            // 数据初始化位置为个人端-教育经历控制层-教育信息分享接口及其内部服务实现
            String eduId = keys[0];
            String eduTxHash = keys[1];

            // 不展示课程和成就所在区块信
            ChainEducationVO chainEducationVO = new ChainEducationVO(
                    eduRecordSolService.select(eduId),
                    clientService.getBlockInfoByTransactionHash(eduTxHash),
                    getChainCourseList(eduId),
                    getChainAchieveList(eduId));
            resList.add(chainEducationVO);
        }

        return resList;
    }

    /**
     * 封装课程数据
     *
     * @param eduId 教育经历ID
     * @return 课程列表
     */
    private List<ChainCourse> getChainCourseList(String eduId) {
        CourseMainInfo courseMainInfo = courseSolService.select(eduId);
        if (Objects.isNull(courseMainInfo)) {
            return null;
        }

        List<String> nameList = courseMainInfo.getNameList();
        List<String> creditList = courseMainInfo.getCreditList();
        List<String> certifyFileList = courseMainInfo.getCertifyFileList();
        List<ChainCourse> resList = new ArrayList<>(nameList.size());

        for (int i = 0; i < nameList.size(); i++) {
            resList.add(new ChainCourse(nameList.get(i), creditList.get(i), certifyFileList.get(i)));
        }

        return resList;
    }

    /**
     * 封装成就数据
     *
     * @param eduId 教育经历ID
     * @return 课程列表
     */
    private List<ChainAchieve> getChainAchieveList(String eduId) {
        AchieveMainInfo achieveMainInfo = achieveSolService.select(eduId);
        if (Objects.isNull(achieveMainInfo)) {
            return null;
        }

        List<String> titleList = achieveMainInfo.getTitleList();
        List<String> acquireTimeList = achieveMainInfo.getAcquireTimeList();
        List<String> certifyFileList = achieveMainInfo.getCertifyFileList();
        List<ChainAchieve> resList = new ArrayList<>(titleList.size());

        for (int i = 0; i < titleList.size(); i++) {
            resList.add(new ChainAchieve(titleList.get(i), acquireTimeList.get(i), certifyFileList.get(i)));
        }

        return resList;
    }
}
