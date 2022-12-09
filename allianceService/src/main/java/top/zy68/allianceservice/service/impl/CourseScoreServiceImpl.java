package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.exception.InternalBusinessException;
import top.zy68.allianceservice.pojo.UpdateScoreBO;
import top.zy68.allianceservice.pojo.dto.CourseIdScoreDTO;
import top.zy68.allianceservice.pojo.dto.StuCourseScoreDTO;
import top.zy68.allianceservice.pojo.vo.CourseScoreVO;
import top.zy68.allianceservice.pojo.vo.StudentCourseScoreVO;
import top.zy68.allianceservice.service.CourseScoreService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.fbChainService.dao.CourseDao;
import top.zy68.fbChainService.dao.CourseScoreDao;
import top.zy68.fbChainService.dao.EduRecordDao;
import top.zy68.fbChainService.dao.StudentDao;
import top.zy68.fbChainService.entity.Course;
import top.zy68.fbChainService.entity.CourseScore;
import top.zy68.fbChainService.entity.EduRecord;
import top.zy68.fbChainService.entity.Student;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * @ClassName CourseScoreServiceImpl
 * @Description 课程成绩服务
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("courseScoreService")
public class CourseScoreServiceImpl implements CourseScoreService {
    @Resource
    private CourseScoreDao courseScoreDao;

    @Resource
    private StudentDao studentDao;

    @Resource
    private EduRecordDao eduRecordDao;

    @Resource
    private CourseDao courseDao;

    /**
     * 获取联盟点内的学生及其课程成绩
     *
     * @param nodeId
     * @return
     */
    @Override
    public List<StudentCourseScoreVO> getAllStudentEduRecords(Integer nodeId) {
        List<Student> studentList = studentDao.queryAllByNodeId(nodeId);
        if (ObjectUtils.isEmpty(studentList)) {
            log.info("联盟点{}还未注册学生", nodeId);
            return null;
        }
        List<StudentCourseScoreVO> res = new ArrayList<>(studentList.size());

        for (Student student : studentList) {
            StudentCourseScoreVO studentCourseScore = new StudentCourseScoreVO();
            // 学生的基本信息
            studentCourseScore.setEduType(student.getEduType())
                    .setIsUplinked(student.getUplinked())
                    .setName(student.getName())
                    .setStuId(student.getStuId())
                    .setPersonId(student.getPersonId());

            // 学生的专业名称
            EduRecord eduRecord = eduRecordDao.queryByEduId(student.getStuId());
            studentCourseScore.setMajorName(eduRecord.getMajorName());

            // 学生的课程信息列表
            List<CourseScore> courseScores = courseScoreDao.queryAllWithCourseByEduId(student.getStuId());
            List<CourseScoreVO> courseScoreList = new ArrayList<>();

            if (!ObjectUtils.isEmpty(courseScores)) {
                for (CourseScore courseScore : courseScores) {
                    CourseScoreVO courseScoreVO = new CourseScoreVO();
                    Course course = courseDao.queryByCourseId(courseScore.getCourseId());
                    if (ObjectUtils.isEmpty(course)) {
                        continue;
                    }
                    courseScoreVO.setCourseId(courseScore.getCourseId())
                            .setIsUplinked(student.getUplinked())
                            .setStuId(student.getStuId())
                            .setName(course.getName())
                            .setScore(courseScore.getScore())
                            .setCredit(course.getCredit());

                    courseScoreList.add(courseScoreVO);
                }
            }
            studentCourseScore.setCourse(courseScoreList);

            res.add(studentCourseScore);
        }

        return res;
    }

    /**
     * 修改一个学生的多门课程成绩
     *
     * @param stuCourseScoreDTO 学生课程成绩
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOneStudentCourseScore(StuCourseScoreDTO stuCourseScoreDTO) {
        List<CourseIdScoreDTO> newStuScoreList = stuCourseScoreDTO.getCourse();
        List<String> failedList = new LinkedList<>();
        // 逐个修改成绩
        for (CourseIdScoreDTO newScore : newStuScoreList) {
            CourseScore originScore = courseScoreDao.queryWithCourseByStuIdAndCourseId(stuCourseScoreDTO.getStuId(), newScore.getCourseId());
            EduRecord eduRecord = eduRecordDao.queryByEduId(stuCourseScoreDTO.getStuId());

            // 查询不到课程成绩记录或课程
            if (ObjectUtils.isEmpty(eduRecord)) {
                throw new ClientBusinessException("不存在该学生的教育记录.");
            } else if (ObjectUtils.isEmpty(originScore) || ObjectUtils.isEmpty(originScore.getCourse())) {
                failedList.add("该学生不存在课程：" + newScore.getCourseId());
                continue;
            }

            // 成绩相等，不修改

            if(originScore.getScore() == null){
                eduRecord.setAcquireCredit(eduRecord.getAcquireCredit() +newScore.getScore());
            }
             else if (Objects.equals(originScore.getScore(),newScore.getScore())) {
                continue;
                // 加学分：原成绩 < 60 && 新成绩 >= 60
            } else if (originScore.getScore() < 60 && newScore.getScore() >= 60) {
                eduRecord.setAcquireCredit(eduRecord.getAcquireCredit() + originScore.getCourse().getCredit());
                // 减学分：原成绩 >= 60 && 新成绩 < 60
            } else if (originScore.getScore() >= 60 && newScore.getScore() < 60) {
                eduRecord.setAcquireCredit(Math.max(0, eduRecord.getAcquireCredit() - originScore.getCourse().getCredit()));
            }

            try {
                courseScoreDao.updateScoreByStuIdAndCourseId(stuCourseScoreDTO.getStuId(), newScore.getCourseId(), (int) newScore.getScore());
                eduRecordDao.updateByEduId(eduRecord.getEduId(), eduRecord);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.info("更改成绩失败：" + e.getMessage());
                failedList.add("更改课程：" + newScore.getCourseId() + " 的成绩失败");
            }
        }

        // 返回更改失败的课程信息
        if (!failedList.isEmpty()) {
            throw new ClientBusinessException(failedList.toString());
        }
    }


    /**
     * 批量导入课程成绩
     *
     * @param scoreDataList 课程成绩列表
     * @param failedList    导入失败数据列表
     */
    @Override
    public void updateScoreBatch(List<UpdateScoreBO> scoreDataList, List<String> failedList) {
        for (UpdateScoreBO updateScoreBO : scoreDataList) {
            Student student = updateScoreBO.getStudent();
            CourseScore originScore = courseScoreDao.queryWithCourseByStuIdAndCourseId(student.getStuId(), updateScoreBO.getCourseId());
            EduRecord eduRecord = eduRecordDao.queryByEduId(student.getStuId());
            String majorId = IdUtil.getMajorIdFromStuId(student.getStuId());

            // 查询不到课程成绩记录或课程
            if (ObjectUtils.isEmpty(originScore)) {
                failedList.add("学生（姓名：" + student.getName() + " 身份证号：" + student.getPersonId() + " 专业编号：" + majorId + "）未选择课程（编号：" + updateScoreBO.getCourseId() + "）或课程不存在");
                continue;
            }
            if (ObjectUtils.isEmpty(eduRecord)) {
                failedList.add("学生（姓名：" + student.getName() + " 身份证号：" + student.getPersonId() + " 专业编号：" + majorId + "）不存在教育经历记录");
                continue;
            }

            // 成绩相等，不修改
            if (Integer.valueOf(originScore.getScore()).equals(updateScoreBO.getScore())) {
                continue;
                // 加学分：原成绩 < 60 && 新成绩 >= 60
            } else if (originScore.getScore() < 60 && updateScoreBO.getScore() >= 60) {
                eduRecord.setAcquireCredit(eduRecord.getAcquireCredit() + originScore.getCourse().getCredit());
                // 减学分：原成绩 >= 60 && 新成绩 < 60
            } else if (originScore.getScore() >= 60 && updateScoreBO.getScore() < 60) {
                eduRecord.setAcquireCredit(Math.max(0, eduRecord.getAcquireCredit() - originScore.getCourse().getCredit()));
            }

            try {
                courseScoreDao.updateScoreByStuIdAndCourseId(student.getStuId(), updateScoreBO.getCourseId(), updateScoreBO.getScore());
                eduRecordDao.updateByEduId(eduRecord.getEduId(), eduRecord);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.info("更改成绩失败：" + e.getMessage());
                failedList.add("录入学生（姓名：" + student.getName() + " 身份证号：" + student.getPersonId() + " 专业编号：" + majorId + "）的课程（课程编号：" + originScore.getCourse().getCourseId() + " 课程名称：" + originScore.getCourse().getName() + "）失败");
            }
        }
    }

    /**
     * 给学生课程添加证书
     *
     * @param eduId    教育经历ID
     * @param courseId 课程编号
     * @param fileId   文件名称
     */
    @Override
    public void updateStuCourseCertify(String eduId, String courseId, String fileId) {
        CourseScore courseScore = courseScoreDao.queryWithCourseByStuIdAndCourseId(eduId, courseId);
        if (ObjectUtils.isEmpty(courseScore.getCertifyUri())) {
            courseScore.setCertifyUri(fileId);
        } else {
            courseScore.setCertifyUri(fileId + ";" + courseScore.getCertifyUri());
        }
        try {
            courseScoreDao.updateById(courseScore);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalBusinessException("文件上传失败.");
        }
    }
}
