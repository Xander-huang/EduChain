package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.exception.InternalBusinessException;
import top.zy68.allianceservice.service.CourseService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.fbChainService.dao.CourseDao;
import top.zy68.fbChainService.dao.CourseScoreDao;
import top.zy68.fbChainService.dao.MajorDao;
import top.zy68.fbChainService.entity.Course;
import top.zy68.fbChainService.entity.Major;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @ClassName CourseServiceImpl
 * @Description 课程服务
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("courseService")
public class CourseServiceImpl implements CourseService {
    @Resource
    CourseDao courseDao;

    @Resource
    CourseScoreDao courseScoreDao;

    @Resource
    MajorDao majorDao;

    /**
     * 生成课程编号
     * 生成策略：随机生成 + 查重
     * 账号规则：专业编号 + 三位数（随机生成）
     *
     * @param majorId 专业编号
     * @return 11个字符的字符串
     */
    @Override
    public String generateCourseId(String majorId) {
        // 默认生成的账号最多重复1000次
        int generateTime = 1000;
        String courseId = null;
        Set<String> courseIdSet = courseDao.queryAllCourseIdByMajorId(majorId);
        for (int i = 0; i < generateTime; i++) {
            courseId = majorId + IdUtil.generateRandomNumber(IdUtil.COURSE_ID);
            // 如果set中没有该id则不重复，直接取用
            if (!courseIdSet.contains(courseId) && courseId.length() == 11) {
                break;
            }
        }
        return courseId;
    }

    /**
     * 通过专业编号查询该专业下对应课程
     *
     * @param majorId 专业编号
     * @return 课程信息列表
     */
    @Override
    public List<Course> queryCourseSetByMajorId(String majorId) {
        return courseDao.queryCourseSetByMajorId(majorId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Course insertCourse(String majorId, String name, short credit) {
        String courseId = generateCourseId(majorId);
        Course course = new Course();
        course.setCourseId(courseId)
                .setName(name)
                .setCredit(credit);

        try {
            courseDao.insertCourse(course);
            majorDao.plusMajorCredit(majorId, (int) credit);
            log.info("成功插入课程信息");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("添加课程失败" + e.getMessage());
            throw new ClientBusinessException("课程添加失败，请重试.");
        }

        return course;
    }


    /**
     * 批量插入课程
     *
     * @param courseList 待插入课程列表
     */
    @Override
    public void insertCourseBatch(List<Course> courseList) {
        if (ObjectUtils.isEmpty(courseList)) {
            log.info("导入课程为空");
            return;
        }
        int row = courseDao.insertBatch(courseList);
        log.info("成功插入{}条数据", row);

        int sumCredit = 0;
        String majorId = IdUtil.getMajoridFromCourseId(courseList.get(0).getCourseId());
        for (Course course : courseList) {
            sumCredit += course.getCredit();
        }
        majorDao.plusMajorCredit(majorId, sumCredit);
        log.info("成功更新专业学分，增加：{} 学分", sumCredit);
    }

    /**
     * 删除课程信息
     *
     * @param courseId 课程编号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteCourse(String courseId) {
        String majorId = IdUtil.getMajoridFromCourseId(courseId);
        Course course = courseDao.queryByCourseId(courseId);
        Major major = majorDao.queryMajorById(majorId);

        if (ObjectUtils.isEmpty(course)) {
            throw new ClientBusinessException("删除失败，不存在该课程.");
        } else if (ObjectUtils.isEmpty(major)) {
            throw new ClientBusinessException("删除失败，不存在该专业.");
        }

        try {
            courseDao.deleteCourse(courseId);
            majorDao.update(majorId, major.getName(), Math.max(0, major.getGraduateCredit() - course.getCredit()));
            log.info("删除课程 {} 成功，专业毕业学分已更新.", courseId);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new InternalBusinessException("删除课程失败.");
        }

        return courseId;
    }

    /**
     * 修改课程信息
     *
     * @param course 课程
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCourse(Course course) {
        Course oldCourse = courseDao.queryByCourseId(course.getCourseId());
        String majorId = IdUtil.getMajoridFromCourseId(course.getCourseId());
        Major major = majorDao.queryMajorById(majorId);

        if (ObjectUtils.isEmpty(oldCourse)) {
            throw new ClientBusinessException("更改失败，不存在该课程.");
        } else if (ObjectUtils.isEmpty(major)) {
            throw new ClientBusinessException("更改失败，不存在该专业.");
        }

        int oldCredit = oldCourse.getCredit();
        int newCredit = course.getCredit();
        int newGraduateCredit = 0;

        // 计算毕业学分
        if (oldCredit > newCredit) {
            newGraduateCredit = Math.max(0, major.getGraduateCredit() - (oldCredit - newCredit));
        } else {
            newGraduateCredit = major.getGraduateCredit() + (newCredit - oldCredit);
        }

        try {
            courseDao.updateCourse(course);
            majorDao.update(major.getMajorId(), major.getName(), newGraduateCredit);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new InternalBusinessException("更新异常，请重试.");
        }
    }

    /**
     * 生成一批课程ID
     *
     * @param majorId    专业编号
     * @param batchCount 数量
     * @return 新课程ID列表
     */
    @Override
    public List<String> generateBatchCourseId(String majorId, int batchCount) {
        Set<String> usedCourseIdSet = courseDao.queryAllCourseIdByMajorId(majorId);
        Set<String> courseIdSet = new HashSet<>(batchCount);

        while (courseIdSet.size() < batchCount) {
            String courseId = majorId + IdUtil.generateRandomNumber(IdUtil.COURSE_ID);
            if (!usedCourseIdSet.contains(courseId) && courseId.length() == 11) {
                courseIdSet.add(courseId);
            }
        }

        return new ArrayList<>(courseIdSet);
    }

    @Override
    public void insertCourseByStu(String eduId, String courseId) {
        int row = courseScoreDao.insertCourseByStu(eduId, courseId);
        if (row == 0) {
            throw new ClientBusinessException();
        }

        log.info("为学生{}添加课程编号为{}的课程", eduId, courseId);
    }

    @Override
    public void deleteCourseByStu(String eduId, String courseId) {
        int row = courseScoreDao.deleteCourseByStu(eduId, courseId);

        if (row == 0) {
            throw new ClientBusinessException();
        }

        log.info("对学生{}删除课程编号为{}的课程", eduId, courseId);
    }
}
