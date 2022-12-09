package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.exception.NormalBusinessException;
import top.zy68.allianceservice.pojo.vo.EduVO;
import top.zy68.allianceservice.pojo.vo.StudentInfoVO;
import top.zy68.allianceservice.service.StudentService;
import top.zy68.allianceservice.util.DateUtil;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.fbChainService.dao.*;
import top.zy68.fbChainService.dto.ScoreDTO;
import top.zy68.fbChainService.entity.*;
import top.zy68.fbChainService.model.chainMainInfo.AchieveMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.CourseMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.EduRecordMainInfo;
import top.zy68.fbChainService.service.AchieveSolService;
import top.zy68.fbChainService.service.CourseSolService;
import top.zy68.fbChainService.service.EduRecordSolService;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;


/**
 * @ClassName StudentServiceImpl
 * @Description 学生服务
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("studentService")
public class StudentServiceImpl implements StudentService {
    @Resource
    StudentDao studentDao;
    @Resource
    PersonDao personDao;
    @Resource
    MajorDao majorDao;
    @Resource
    EduRecordDao eduRecordDao;
    @Resource
    AllianceNodeDao allianceNodeDao;
    @Resource
    CourseScoreDao courseScoreDao;
    @Resource
    CourseDao courseDao;
    @Resource
    AchieveDao achieveDao;
    @Resource
    EduRecordSolService eduRecordSolService;
    @Resource
    AchieveSolService achieveSolService;
    @Resource
    CourseSolService courseSolService;

    /**
     * 生成学生编号
     * 账号规则：专业编号 + UUID
     * 生成策略：UUID生成
     *
     * @param majorId 专业编号
     * @return 40个字符的字符串
     */
    @Override
    public String generateStuId(String majorId) {
        return majorId + IdUtil.getUUID();
    }

    /**
     * 添加一个学生
     *
     * @param personId 身份证号
     * @param majorId  专业编号
     * @return 新增学生的信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentInfoVO addStudent(String personId, String majorId) {
        String stuId = generateStuId(majorId);
        Person person = personDao.queryPersonByPersonId(personId);
        Major major = majorDao.queryMajorById(majorId);
        AllianceNode allianceNode = allianceNodeDao.queryByNodeId(IdUtil.getNodeId(majorId));
        Set<String> courseIds = courseDao.queryAllCourseIdByMajorId(majorId);
        List<ScoreDTO> bindCourseList = new ArrayList<>(courseIds.size());

        if (ObjectUtils.isEmpty(person)) {
            throw new ClientBusinessException("录入学生失败，不存在该学习者.");
        } else if (ObjectUtils.isEmpty(major)) {
            throw new ClientBusinessException("录入学生失败，不存在该专业.");
        } else if (ObjectUtils.isEmpty(allianceNode)) {
            throw new ClientBusinessException("录入学生失败，不存在该机构.");
        }

        Student student = new Student()
                .setStuId(stuId)
                .setName(person.getName())
                .setEduType(major.getType())
                .setPersonId(personId)
                .setUplinked((short) 0);
        EduRecord eduRecord = new EduRecord()
                .setEduId(stuId)
                .setPersonId(personId)
                .setType(major.getType())
                .setBeginTime(DateUtil.getCurrYear())
                .setNodeId(allianceNode.getNodeId())
                .setNodeName(allianceNode.getName())
                .setAcquireCredit(0)
                .setGraduateCredit(major.getGraduateCredit())
                .setMajorName(major.getName());

        for (String courseId : courseIds) {
            bindCourseList.add(new ScoreDTO(stuId, courseId, (short) 0));
        }

        try {
            studentDao.insertStudent(student);
            eduRecordDao.insert(eduRecord);
            // 批量绑定课程
            if (!CollectionUtils.isEmpty(bindCourseList) ||  bindCourseList.size() > 0){
                courseScoreDao.insertCourseScoreBatch(bindCourseList);
            }
            log.info("成功添加一个学生：{}", personId);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("添加学生失败：" + e.getMessage());
            throw new ClientBusinessException("添加学生失败，请重试.");
        }

        return new StudentInfoVO()
                .setStuId(stuId)
                .setPersonId(personId)
                .setName(person.getName())
                .setMajorName(major.getName())
                .setEduType(major.getType())
                .setUplinked((short) 0);
    }

    /**
     * 删除一个学生
     * 实际是删除该学号所对应的所有数据
     *
     * @param stuId 学号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteStudent(String stuId) {
        // 删学号/教育经历ID所绑定的表
        try {
            studentDao.deleteStudentByStuId(stuId);
            eduRecordDao.deleteEduRecord(stuId);
            courseScoreDao.deleteAllByEduId(stuId);
            achieveDao.deleteAchieveByEduId(stuId);
            log.info("成功删除一个学生：{}的所有关联信息", stuId);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("删除学生失败：" + e.getMessage());
            throw new ClientBusinessException("删除学生失败，请重试.");
        }
        return stuId;
    }

    /**
     * 批量添加学生
     *
     * @param studentList    新学生列表
     * @param nodeId         机构编号
     * @param cachedMajorMap 专业编号和专业对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertStudentBatch(List<Student> studentList, int nodeId, Map<String, Major> cachedMajorMap) {
        AllianceNode allianceNode = allianceNodeDao.queryByNodeId(nodeId);

        for (Student student : studentList) {
            String majorId = IdUtil.getMajorIdFromStuId(student.getStuId());
            Major major = cachedMajorMap.get(majorId);
            Set<String> courseIds = courseDao.queryAllCourseIdByMajorId(majorId);
            List<ScoreDTO> bindCourseList = new ArrayList<>(courseIds.size());

            for (String courseId : courseIds) {
                bindCourseList.add(new ScoreDTO(student.getStuId(), courseId, (short) 0));
            }

            EduRecord eduRecord = new EduRecord()
                    .setEduId(student.getStuId())
                    .setPersonId(student.getPersonId())
                    .setType(student.getEduType())
                    .setBeginTime(DateUtil.getCurrYear())
                    .setNodeId(allianceNode.getNodeId())
                    .setNodeName(allianceNode.getName())
                    .setAcquireCredit(0)
                    .setGraduateCredit(major.getGraduateCredit())
                    .setMajorName(major.getName());

            try {
                studentDao.insertStudent(student);
                eduRecordDao.insert(eduRecord);
                // 批量绑定课程
                courseScoreDao.insertCourseScoreBatch(bindCourseList);
                log.info("成功添加学生：{}", student.getName());
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.warn("添加学生 {} 失败：{}", student.getName(), e.getMessage());
            }
        }
    }

    /**
     * 更改学生专业
     * 注：1. 生成新学号（更改专业编号段）、2. 更改教育经历、3. 更改成就经历ID、4. 清除旧专业所学课程数据、5. 更改学生表信息
     *
     * @param stuId   学生
     * @param majorId 新专业编号
     * @return 新旧学号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, String> changeStudentMajor(String stuId, String majorId) {
        // 1. 新学号为原学号用新专业id替换旧专业id
        Student student = studentDao.queryByStuId(stuId);
        EduRecord eduRecord = eduRecordDao.queryByEduId(stuId);
        Major major = majorDao.queryMajorById(majorId);
        Set<String> courseIds = courseDao.queryAllCourseIdByMajorId(majorId);

        if (ObjectUtils.isEmpty(student)) {
            throw new ClientBusinessException("转专业失败，不存在该学生.");
        } else if (ObjectUtils.isEmpty(eduRecord)) {
            throw new ClientBusinessException("转专业失败，不存在该学生.");
        } else if (ObjectUtils.isEmpty(major)) {
            throw new ClientBusinessException("转专业失败，不存在该专业.");
        }

        List<ScoreDTO> bindCourseList = new ArrayList<>(courseIds.size());
        Map<String, String> resMap = new HashMap<>(2);
        String newStuId = majorId + IdUtil.getUUIDFromStuId(stuId);

        eduRecord.setEduId(newStuId)
                .setMajorName(major.getName())
                .setType(major.getType())
                .setAcquireCredit(0)
                .setGraduateCredit(major.getGraduateCredit());
        student.setStuId(newStuId).setEduType(major.getType());
        for (String courseId : courseIds) {
            bindCourseList.add(new ScoreDTO(stuId, courseId, (short) 0));
        }

        try {
            // 2. 教育经历信息的更改
            eduRecordDao.updateByEduId(stuId, eduRecord);
            // 3. 成就ID的更改
            achieveDao.updateEduId(stuId, newStuId);
            // 4. 所参加课程的删除
            courseScoreDao.deleteAllByEduId(stuId);
            // 5. 更改学生信息
            studentDao.updateByStuId(stuId, student);
            // 6. 绑定新专业的课程
            courseScoreDao.insertCourseScoreBatch(bindCourseList);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("给学生转专业失败：" + e.getMessage());
            throw new ClientBusinessException("学生转专业失败，请重试！");
        }

        resMap.put("origin", stuId);
        resMap.put("new", newStuId);

        return resMap;
    }

    /**
     * 获取联盟点内所有学生
     *
     * @param nodeId 联盟点编号
     * @return 学生信息列表
     */
    @Override
    public List<StudentInfoVO> getAllStudents(Integer nodeId) {
        List<Student> studentList = studentDao.queryAllByNodeId(nodeId);
        if (ObjectUtils.isEmpty(studentList)) {
            log.info("该机构还未注册学生");
            throw new NormalBusinessException("该机构还未注册学生");
        }
        List<StudentInfoVO> studentInfoVOList = new ArrayList<>(studentList.size());
        for (Student student : studentList) {
            Major major = majorDao.queryMajorById(IdUtil.getMajorIdFromStuId(student.getStuId()));
            if (ObjectUtils.isEmpty(major)) {
                log.info("不存在该专业");
                continue;
            }
            StudentInfoVO studentInfoVO = new StudentInfoVO()
                    .setStuId(student.getStuId())
                    .setPersonId(student.getPersonId())
                    .setName(student.getName())
                    .setMajorName(major.getName())
                    .setEduType(student.getEduType())
                    .setUplinked(student.getUplinked());
            studentInfoVOList.add(studentInfoVO);
        }
        return studentInfoVOList;
    }

    /**
     * 获取联盟点内所有的专业信息，封装为 专业编号 - 专业对象 的形式
     *
     * @param nodeId 联盟点编号
     * @return map
     */
    @Override
    public Map<String, Major> getMajorKeyValueMap(Integer nodeId) {
        List<Major> majorList = majorDao.queryAllByNodeId(nodeId);
        Map<String, Major> majorMapper = new HashMap<>(majorList.size());

        if (ObjectUtils.isEmpty(majorList)) {
            throw new NormalBusinessException("该机构还未开设专业.");
        }

        for (Major major : majorList) {
            majorMapper.put(major.getMajorId(), major);
        }

        return majorMapper;
    }

    /**
     * 批量上链学生教育记录
     * 上链内容：教育记录 + 所有的成就 + 所学的课程信息
     * 上链条件：已获学分 >= 毕业要求学分
     * 上链逻辑：
     * 1. 不考虑区块链模块本身问题：仅给予一次上链机会。一旦全部数据上链成功将不允许再次上链，同时学生信息也被删除。
     * 2. 考虑区块链模块本身问题（如网络）导致部分上链成功的情况：学生数据上链结果为失败，但已上链成功部分保存交易哈希到数据库，未成功部分允许用户继续尝试提交至成功（已成功部分不再上链）。
     *
     * @param stuIdList 上链学生学号
     * @return 成功上链学生ID列表
     */
    @Override
    public List<String> uplinkStuEduInfo(String[] stuIdList) {
        List<String> succeedStuList = new LinkedList<>();

        for (String stuId : stuIdList) {
            EduRecord eduRecord = eduRecordDao.queryByEduId(stuId);
            if (ObjectUtils.isEmpty(eduRecord)) {
                log.info("不存在ID为 {} 的教育记录.", stuId);
                continue;
            }

            // 如果学分未达到毕业要求学分，不上链
            if (eduRecord.getAcquireCredit() < eduRecord.getGraduateCredit()) {
                log.info("学生 {} 学分未达毕业要求，数据未上链.", stuId);
                continue;
            }

            String eduRecordTransactionHash = eduRecord.getEduTransactionHash();
            // 未上过链才给上链，已经上过链的不再上链 = 主要用户有部分数据因为网络原因无法上链导致失败的情况。
            if (ObjectUtils.isEmpty(eduRecordTransactionHash)) {
                // 上链教育经历封装
                EduRecordMainInfo eduRecordMainInfo = new EduRecordMainInfo(
                        eduRecord.getEduId(),
                        eduRecord.getPersonId(),
                        eduRecord.getType(),
                        BigInteger.valueOf(eduRecord.getNodeId()),
                        eduRecord.getNodeName(),
                        String.valueOf(eduRecord.getBeginTime()),
                        String.valueOf(DateUtil.getCurrYear()),
                        eduRecord.getMajorName(),
                        BigInteger.valueOf(eduRecord.getAcquireCredit()),
                        BigInteger.valueOf(eduRecord.getGraduateCredit()),
                        eduRecord.getCertifyUri() == null ? "null" : eduRecord.getCertifyUri()
                );
                eduRecordTransactionHash = eduRecordSolService.insert(eduRecordMainInfo);
                if (ObjectUtils.isEmpty(eduRecordTransactionHash)) {
                    log.error("学生：{} 的教育经历上链失败", stuId);
                }
            }

            String achievesTransactionHash = eduRecord.getAchievesTransactionHash();
            if (ObjectUtils.isEmpty(achievesTransactionHash)) {
                // 上链成就数据封装
                AchieveMainInfo achieveMainInfo = packingAchieveMainInfo(stuId);
                if (!ObjectUtils.isEmpty(achieveMainInfo)) {
                    // 获得过成就
                    achievesTransactionHash = achieveSolService.insert(achieveMainInfo);
                } else {
                    // 没有获得成就，交易哈希为 "no achieve"
                    achievesTransactionHash = "no achieve";
                }
                if (ObjectUtils.isEmpty(achievesTransactionHash)) {
                    log.error("学生：{} 的成就数据上链失败", stuId);
                }
            }

            String coursesTransactionHash = eduRecord.getCoursesTransactionHash();
            if (ObjectUtils.isEmpty(coursesTransactionHash)) {
                // 上链课程数据封装
                CourseMainInfo courseMainInfo = packingCourseMainInfo(stuId);
                coursesTransactionHash = courseSolService.insert(courseMainInfo);
                if (ObjectUtils.isEmpty(coursesTransactionHash)) {
                    log.error("学生：{} 的课程数据上链失败", stuId);
                }
            }

            // case1：全部上链失败
            if (ObjectUtils.isEmpty(eduRecordTransactionHash) && ObjectUtils.isEmpty(achievesTransactionHash) && ObjectUtils.isEmpty(coursesTransactionHash)) {
                log.warn("学生{}数据上链全部失败.", stuId);
                continue;
            }

            // (case2 & case3)(只要有一个数据上链成功)：将交易哈希值保存至数据库
            eduRecord.setEndTime(DateUtil.getCurrYear());
            eduRecord.setEduTransactionHash(eduRecordTransactionHash);
            eduRecord.setCoursesTransactionHash(coursesTransactionHash);
            eduRecord.setAchievesTransactionHash(achievesTransactionHash);
            eduRecordDao.updateByEduId(stuId, eduRecord);

            // case2：只要有一个没上链成功，结果都是该学生上链失败（即不将该学号添加至返回成功列表）。（但需要保存已经成功上链成功的数据的交易哈希值，上面已保存）
            if (ObjectUtils.isEmpty(eduRecordTransactionHash) || ObjectUtils.isEmpty(achievesTransactionHash) || ObjectUtils.isEmpty(coursesTransactionHash)) {
                continue;
            }

            // case3：全部上链成功,删除学生数据
            removeStu(stuId);
            succeedStuList.add(stuId);
        }

        return succeedStuList;
    }

    /**
     * 删除学生数据
     *
     * @param stuId 学号
     */
    private void removeStu(String stuId) {
        try {
            // 删除学生信息
            studentDao.deleteStudentByStuId(stuId);
            // 删除学生成就
            achieveDao.deleteAchieveByEduId(stuId);
            // 删除学生课程成绩
            courseScoreDao.deleteAllByEduId(stuId);
        } catch (Exception e) {
            log.warn("DAO数据库操作异常，删除已上链学生: {} 数据失败.", stuId);
            e.printStackTrace();
        }
    }

    /**
     * 获取学生在联盟内教育id
     *
     * @param personId 身份证号
     * @return 教育信息列表
     */
    @Override
    public List<EduVO> getStudentIdAndMajor(String personId) {
        List<EduRecord> eduRecords = eduRecordDao.queryAllByPersonId(personId);
        List<EduVO> result = new LinkedList<>();

        if (ObjectUtils.isEmpty(eduRecords)) {
            return result;
        }

        for (EduRecord eduRecord : eduRecords) {
            // 返回未上链的经历信息
            if (ObjectUtils.isEmpty(eduRecord.getEduTransactionHash())) {
                result.add(new EduVO(eduRecord.getEduId(), eduRecord.getType(), eduRecord.getMajorName()));
            }
        }

        return result;
    }

    /**
     * 封装用于上链的成就数据
     *
     * @param eduId 教育经历ID
     * @return 成就数据
     */
    private AchieveMainInfo packingAchieveMainInfo(String eduId) {
        List<Achieve> achieveList = achieveDao.queryPassedByEduId(eduId);
        if (ObjectUtils.isEmpty(achieveList)) {
            log.error("教育经历：{} 不存在成就", eduId);
            return null;
        }

        AchieveMainInfo achieveMainInfo = new AchieveMainInfo();
        List<String> titleList = new LinkedList<>();
        List<String> acquireTimeList = new LinkedList<>();
        List<String> fileList = new LinkedList<>();

        for (Achieve achieve : achieveList) {
            // 审批通过的成就
            if (achieve.getAuditStatus() == 1) {
                titleList.add(achieve.getTitle());
                acquireTimeList.add(DateUtil.dateToString(achieve.getAcquireTime(), DateUtil.PATTERN_DATE));
                // 如果没有证书，存入null
                fileList.add(achieve.getCertifyUri() == null ? "null" : achieve.getCertifyUri());
            }
        }

        achieveMainInfo.setEduId(eduId);
        achieveMainInfo.setTitleList(titleList);
        achieveMainInfo.setAcquireTimeList(acquireTimeList);
        achieveMainInfo.setCertifyFileList(fileList);

        return achieveMainInfo;
    }

    /**
     * 封装用于上链的课程数据
     *
     * @param eduId 教育经历ID
     * @return 课程数据
     */
    private CourseMainInfo packingCourseMainInfo(String eduId) {
        List<CourseScore> courseScoreList = courseScoreDao.queryAllWithCourseByEduId(eduId);
        // 只要能进入到打包上链课程数据这一步，就一定有课程分数
        int courseNum = courseScoreList.size();

        CourseMainInfo courseMainInfo = new CourseMainInfo();

        List<String> nameList = new ArrayList<>(courseNum);
        List<String> creditList = new ArrayList<>(courseNum);
        List<String> fileList = new ArrayList<>(courseNum);

        for (CourseScore courseScore : courseScoreList) {
            if (courseScore.getScore() >= 60) {
                nameList.add(courseScore.getCourse().getName());
                creditList.add(String.valueOf(courseScore.getCourse().getCredit()));
                // 如果没有证书，存入null
                fileList.add(courseScore.getCertifyUri() == null ? "null" : courseScore.getCertifyUri());
            } else {
                log.info("学生: {} 课程: {} 成绩: {}, 不及格，不上链.", courseScore.getEduId(), courseScore.getCourseId(), courseScore.getScore());
            }
        }

        courseMainInfo.setEduId(eduId);
        courseMainInfo.setNameList(nameList);
        courseMainInfo.setCreditList(creditList);
        courseMainInfo.setCertifyFileList(fileList);

        return courseMainInfo;
    }
}
