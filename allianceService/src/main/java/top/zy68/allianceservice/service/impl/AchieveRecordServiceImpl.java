package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.pojo.vo.AchieveVO;
import top.zy68.allianceservice.service.AchieveRecordService;
import top.zy68.allianceservice.util.DateUtil;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.fbChainService.dao.AchieveDao;
import top.zy68.fbChainService.dao.MajorDao;
import top.zy68.fbChainService.dao.StudentDao;
import top.zy68.fbChainService.entity.Achieve;
import top.zy68.fbChainService.entity.Major;
import top.zy68.fbChainService.entity.Student;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName AchieveRecordServiceImpl
 * @Description 成就记录服务
 * @Author Sustart
 * @Date2022/3/15 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("achieveRecordService")
public class AchieveRecordServiceImpl implements AchieveRecordService {
    @Resource
    AchieveDao achieveDao;
    @Resource
    MajorDao majorDao;
    @Resource
    StudentDao studentDao;

    /**
     * 查询所有学生的所有成就
     *
     * @param nodeNumber 联盟点编号
     * @return 成就列表
     */
    @Override
    public List<AchieveVO> queryAllAchieves(Integer nodeNumber) {
        List<Student> students = studentDao.queryAllByNodeId(nodeNumber);
        if (ObjectUtils.isEmpty(students)) {
            log.info("未查询到学生");
            return null;
        }
        List<AchieveVO> allAchieves = new ArrayList<>();
        for (Student student : students) {
            List<Achieve> achieves = achieveDao.queryAllByEduId(student.getStuId());
            for (Achieve achieve : achieves) {
                AchieveVO tmp = new AchieveVO();
                Major major = majorDao.queryMajorById(IdUtil.getMajorIdFromStuId(student.getStuId()));
                tmp.setFeedback(achieve.getFeedback())
                        .setId(achieve.getId())
                        .setTitle(achieve.getTitle())
                        .setRemark(achieve.getRemark())
                        .setType(achieve.getType())
                        .setCertifyUri(achieve.getCertifyUri())
                        .setAcquireTime(DateUtil.dateToString(achieve.getAcquireTime(), DateUtil.PATTERN_DATE))
                        .setAuditStatus(achieve.getAuditStatus())
                        .setName(student.getName())
                        .setPersonId(student.getPersonId())
                        .setEduId(student.getStuId())
                        .setEduType(student.getEduType())
                        .setMajorName(major.getName());
                allAchieves.add(tmp);
            }
        }
        return allAchieves;
    }

    /**
     * 修改认证成就状态
     *
     * @param id          成就编号
     * @param auditStatus 审批状态
     * @param feedback    反馈信息
     */
    @Override
    public void updateAchievesStatus(Integer id, Short auditStatus, String feedback) {
        Achieve achieve = achieveDao.queryById(id);
        if (ObjectUtils.isEmpty(achieve)) {
            throw new ClientBusinessException("该成就不存在.");
        }

        achieve.setFeedback(feedback);
        achieve.setAuditStatus(auditStatus);
        achieveDao.updateAchieve(achieve);
    }

    /**
     * 联盟用户添加学生成就
     *
     * @param eduId       学号
     * @param title       标题
     * @param type        类型
     * @param acquireTime 获得时间
     * @return 添加成功的成就
     */
    @Override
    public Achieve insertAchieves(String eduId, String title, String type, Date acquireTime) {
        Achieve achieve = new Achieve();
        Student student = studentDao.queryByStuId(eduId);
        if (ObjectUtils.isEmpty(student)) {
            throw new ClientBusinessException("不存在该学生");
        }
        // 机构录入免审批
        achieve.setEduId(eduId)
                .setType(type)
                .setTitle(title)
                .setAcquireTime(acquireTime)
                .setAuditStatus((short) 1)
                .setFeedback("由机构添加.");
        achieveDao.insertAnAchieve(achieve);
        return achieve;
    }
}
