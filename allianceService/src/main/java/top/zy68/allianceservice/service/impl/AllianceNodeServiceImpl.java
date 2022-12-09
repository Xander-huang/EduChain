package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.exception.InternalBusinessException;
import top.zy68.allianceservice.exception.NormalBusinessException;
import top.zy68.allianceservice.pojo.dto.NodeRegisterDTO;
import top.zy68.allianceservice.pojo.vo.IntListVO;
import top.zy68.allianceservice.pojo.vo.NodeDataVO;
import top.zy68.allianceservice.pojo.vo.StrFloatMapVO;
import top.zy68.allianceservice.pojo.vo.StrIntMapVO;
import top.zy68.allianceservice.service.AllianceNodeService;
import top.zy68.allianceservice.util.DateUtil;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.allianceservice.util.JwtUtil;
import top.zy68.fbChainService.config.Mail;
import top.zy68.fbChainService.dao.*;
import top.zy68.fbChainService.dto.IntKvDTO;
import top.zy68.fbChainService.dto.StrFloatKvDTO;
import top.zy68.fbChainService.entity.Major;
import top.zy68.fbChainService.entity.NodeUser;
import top.zy68.fbChainService.service.MailService;

import javax.annotation.Resource;
import java.util.*;


/**
 * @ClassName AllianceNodeServiceImpl
 * @Description 联盟点服务
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("allianceNodeService")
public class AllianceNodeServiceImpl implements AllianceNodeService {

    @Resource
    AllianceNodeDao allianceNodeDao;
    @Resource
    StudentDao studentDao;
    @Resource
    MajorDao majorDao;
    @Resource
    CourseDao courseDao;
    @Resource
    EduRecordDao eduRecordDao;
    @Resource
    CourseScoreDao courseScoreDao;
    @Resource
    NodeUserDao nodeUserDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedisTemplate<String, NodeRegisterDTO> redisTemplate;
    @Resource
    MailService mailService;
    @Resource
    Mail mailDO;
    @Value("${fbAdmin.email}")
    String adminEmail;
    @Value("${system.ip}")
    String ip;

    /**
     * 注册一个机构及其管理员的通知
     *
     * @param registerDTO 注册DTO
     * @return 注册返回消息
     */
    @Override
    public String registerNodeNotification(NodeRegisterDTO registerDTO) {
        String mailCaptcha = stringRedisTemplate.opsForValue().get(registerDTO.getEmail());
        if (mailCaptcha == null || mailCaptcha.length() == 0 || !Objects.equals(mailCaptcha, registerDTO.getCaptcha())) {
            log.warn("注册用户的验证码不存在或已过期");
            throw new ClientBusinessException("验证码不存在或已过期");
        }

        stringRedisTemplate.delete(registerDTO.getEmail());

        // redisTemplate序列化设置
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());

        String key = IdUtil.getUUID();
        redisTemplate.opsForValue().set(key, registerDTO);



        this.auditRegister(key,"permit");


        //todo:不用发邮件，直接注册成功
//        sendAdminEmailToAudit(key, registerDTO);
//        log.info("一个机构申请注册平台，已发送至管理审批.");

        return "机构"+key+"注册成功";
    }

    /**
     * 向管理员提交审批申请
     *
     * @param key         redis key
     * @param registerDTO 注册对象
     */
    private void sendAdminEmailToAudit(String key, NodeRegisterDTO registerDTO) {



        String nodeType;
        if (registerDTO.getType() == 0) {
            nodeType = "高校";
        } else {
            nodeType = "培训机构";
        }

        String subject = "终身教育服务平台-机构审批";
        String text = "管理员，您好：\n"
                + "机构名称：" + registerDTO.getNodeName()
                + "\n类型：" + nodeType
                + "\n联系人：" + registerDTO.getUserName()
                + "\n手机号码：" + registerDTO.getPhone()
                + "\n邮箱：" + registerDTO.getEmail()
                + "\n希望注册使用终身教育服务平台，请予以审批："
                + "\n同意：http://"+ip+":8083/alliance/doAudit/" + key + "/permit"
                + "\n拒绝：http://"+ip+":8083/alliance/doAudit/" + key + "/reject" + "\n";



        mailDO.setTo(adminEmail);
        mailDO.setSubject(subject);
        mailDO.setText(text);
        mailService.sendMail(mailDO);

        if (!mailDO.getStatus()) {
            log.warn("Failed to send a mail to Admin: {}. Message:{}", adminEmail, mailDO.getError());
            throw new InternalBusinessException("注册申请提交失败，请联系管理员.");
        }
        log.info("Succeed to send a mail to Admin: " + adminEmail);
    }

    /**
     * 审批机构注册
     *
     * @param key      redis key
     * @param auditRes 结果
     * @return 审批结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String auditRegister(String key, String auditRes) {
        NodeRegisterDTO nodeRegisterDTO = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(nodeRegisterDTO)) {
            return "不存在该审批事务";
        }
        redisTemplate.delete(key);

        String text;

        //    拒绝
        if (Objects.equals("reject", auditRes)) {
            text = "先生/女生，您好。很抱歉，申请注册终身教育服务平台已被拒绝.";
            sendAuditResByEmail(nodeRegisterDTO.getEmail(), text);
            return "审批完成：已拒绝";
        }

        // 存入机构信息
        int nodeId = generateNodeId();
        // 存入用户
        String userId = nodeId + "00";
        String salt = JwtUtil.generateSalt();
        String encryptPwd = new Sha256Hash(nodeRegisterDTO.getPassword(), salt).toHex();
        NodeUser nodeUser = new NodeUser()
                .setAccount(userId)
                .setPassword(encryptPwd)
                .setSalt(salt)
                .setName(nodeRegisterDTO.getUserName())
                .setEmail(nodeRegisterDTO.getEmail())
                .setPhone(nodeRegisterDTO.getPhone())
                .setRole("admin");
        try {
            allianceNodeDao.insertNode(nodeId, nodeRegisterDTO.getNodeName(), nodeRegisterDTO.getType());
            nodeUserDao.insertNodeUser(nodeUser);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("关键字段已存在：" + e.getMessage());
            text = "先生/女生，您好！终身教育服务平台管理员已同意贵单位的注册请求，但机构名称或负责人手机号码或邮箱已存在，请更改后重新申请注册。"
                    + "\n机构名称：" + nodeRegisterDTO.getNodeName()
                    + "\n联系人：" + nodeRegisterDTO.getUserName()
                    + "\n手机号码：" + nodeRegisterDTO.getPhone()
                    + "\n邮箱：" + nodeRegisterDTO.getEmail()
                    + "\n";
            sendAuditResByEmail(nodeRegisterDTO.getEmail(), text);
            throw new NormalBusinessException("申请注册的单位名称已存在，已通知注册者更改.");
        }
        log.info("成功新增一个机构：{}.", nodeRegisterDTO.getNodeName());
        log.info("成功新增一位机构的一级管理员：{}.", nodeRegisterDTO.getUserName());

        text = "先生/女生，您好。注册终身教育服务平台管理员已同意贵单位的注册请求，请前往 website.com 使用注册时的邮箱或账号：" + userId + "和密码进行登录。如需平台使用手册，请联系 1501024528@qq.com.";
        sendAuditResByEmail(nodeRegisterDTO.getEmail(), text);
        return "审批完成：已通过";
    }

    /**
     * 生成一个联盟点id
     *
     * @return 联盟点编号
     */
    private Integer generateNodeId() {
        // 默认生成的账号最多重复1000次
        int generateTime = 1000;
        int nodeId = 0;
        Set<Integer> nodeIdSet = allianceNodeDao.queryAllNodeId();
        for (int i = 0; i < generateTime; i++) {
            String nodeIdStr = IdUtil.generateRandomNumber(IdUtil.NODE_ID);
            nodeId = Integer.parseInt(nodeIdStr);
            // 如果set中没有该id则不重复，直接取用
            if (!nodeIdSet.contains(nodeId) && nodeIdStr.length() == 5) {
                break;
            }
        }
        return nodeId;
    }

    /**
     * 通过邮件告知审批结果
     *
     * @param email 接收邮箱
     * @param text  邮件内容
     */
    private void sendAuditResByEmail(String email, String text) {

        String subject = "终身教育服务平台";
        mailDO.setTo(email);
        mailDO.setSubject(subject);
        mailDO.setText(text);
        mailService.sendMail(mailDO);

        if (!mailDO.getStatus()) {
            log.warn("Failed to send a mail to Admin: {}. Message:{}", email, mailDO.getError());
            throw new InternalBusinessException("注册申请提交失败，请联系管理员.");
        }
        log.info("Succeed to send a mail to Admin: " + email);
    }

    /**
     * 获取节点相关数据
     *
     * @param nodeId 联盟点编号
     * @return 节点数据封装视图对象
     */
    @Override
    public NodeDataVO getNodeData(Integer nodeId) {
        Integer student = studentDao.countNodeStu(nodeId);
        Integer major = majorDao.countNodeMajor(nodeId);
        Integer course = courseDao.countNodeCourse(nodeId);
        Integer uplinked = eduRecordDao.countNodeUplinkedStuNum(nodeId);
        IntListVO annualEnrollment = getInListVO(eduRecordDao.countAnnualNewStu(nodeId));
        IntListVO annualGraduation = getInListVO(eduRecordDao.countAnnualUplinkedStu(nodeId));
        IntListVO annualMajor = getInListVO(majorDao.countAnnualNewMajor(nodeId));
        IntListVO annualCourse = getInListVO(courseDao.countAnnualNewCourse(nodeId));

        StrFloatMapVO majorAverage = getMajorAverage(nodeId);
        StrIntMapVO majorNum = getMajorNum(nodeId);
        List<Integer> education = getEducation(nodeId);

        return new NodeDataVO(student, major, course, uplinked, annualEnrollment, annualGraduation, annualMajor, annualCourse, majorAverage, majorNum, education);
    }


    /**
     * 根据DAO方法查询结果封装数据
     *
     * @param dataList 数据列表
     * @return 封装的列表对象
     */
    private IntListVO getInListVO(List<IntKvDTO> dataList) {
        if (ObjectUtils.isEmpty(dataList)) {
            return null;
        }
        List<Integer> keyList = new ArrayList<>(dataList.size());
        List<Integer> valueList = new ArrayList<>(dataList.size());

        for (IntKvDTO intKvDTO : dataList) {
            keyList.add(intKvDTO.getDataKey());
            valueList.add(intKvDTO.getDataValue());
        }

        return new IntListVO(keyList, valueList);
    }

    /**
     * 计算当前年份专业课程平均分排行前8的专业及分数，由高到低排列
     *
     * @param nodeId 联盟点编号
     * @return Map的两个列表数据
     */
    private StrFloatMapVO getMajorAverage(Integer nodeId) {
        Integer currYear = DateUtil.getCurrYear();
        int priorityMajorNum = 8;
        List<StrFloatKvDTO> dataList = courseScoreDao.queryMajorIdAndAvgCourseScore(currYear, nodeId, priorityMajorNum);
        if (ObjectUtils.isEmpty(dataList)) {
            log.info("联盟点：{} 还未添加课程成绩.", nodeId);
            return null;
        }

        List<String> majorNameList = new ArrayList<>(dataList.size());
        List<Float> majorAvgCourseScoreList = new ArrayList<>(dataList.size());

        for (StrFloatKvDTO data : dataList) {
            Major major = majorDao.queryMajorById(String.valueOf(data.getDataKey()));
            // todo sql优化
            if (ObjectUtils.isEmpty(major)) {
                log.info("联盟点：{} 不存在该 {} 专业.", nodeId, data.getDataKey());
                continue;
            }
            majorNameList.add(major.getName());
            majorAvgCourseScoreList.add(data.getDataValue());
        }

        return new StrFloatMapVO(majorNameList, majorAvgCourseScoreList);
    }

    /**
     * 计算当前年份专业人数数量前8，由高到低排列
     *
     * @param nodeId 联盟点编号
     * @return Map的两个列表数据
     */
    private StrIntMapVO getMajorNum(Integer nodeId) {
        Integer currYear = DateUtil.getCurrYear();
        int priorityMajorNum = 8;
        List<IntKvDTO> dataList = eduRecordDao.countCurrYearMajorStuNum(currYear, nodeId, priorityMajorNum);
        if (ObjectUtils.isEmpty(dataList)) {
            log.info("联盟点：{} 还未开设专业.", nodeId);
            return null;
        }

        List<String> majorNameList = new ArrayList<>(dataList.size());
        List<Integer> majorStuNumList = new ArrayList<>(dataList.size());

        for (IntKvDTO data : dataList) {
            Major major = majorDao.queryMajorById(String.valueOf(data.getDataKey()));
            // todo sql优化
            if (ObjectUtils.isEmpty(major)) {
                log.info("联盟点：{} 不存在该 {} 专业.", nodeId, data.getDataKey());
                continue;
            }
            majorNameList.add(major.getName());
            majorStuNumList.add(data.getDataValue());
        }

        return new StrIntMapVO(majorNameList, majorStuNumList);
    }

    /**
     * 当前年份，按学生平均成绩分类计算的学生数量，分为四类：平均成绩>90, 平均成绩>80, 平均成绩>60, 平均成绩<60,
     *
     * @param nodeId 联盟点编号
     * @return Map的两个列表数据
     */
    private List<Integer> getEducation(Integer nodeId) {
        Integer currYear = DateUtil.getCurrYear();
        List<Integer> resList = new ArrayList<>(4);
        // 100 >= x > 90
        int a = courseScoreDao.countStuNumByAvgScoreInRange(currYear, nodeId, 100, 90);
        resList.add(a);

        // 90 >= x > 80
        int b = courseScoreDao.countStuNumByAvgScoreInRange(currYear, nodeId, 90, 80);
        resList.add(b);

        // 80 >= x > 60
        int c = courseScoreDao.countStuNumByAvgScoreInRange(currYear, nodeId, 80, 60);
        resList.add(c);

        // x < 60
        int d = courseScoreDao.countStuNumByAvgScoreLessThan(currYear, nodeId, 60);
        resList.add(d);

        return resList;
    }
}
