package top.zy68.allianceservice.service;

import top.zy68.fbChainService.entity.Course;

import java.util.List;

/**
 * @ClassName CourseService
 * @Description 节点课程服务
 * @Author Sustart
 * @Date2022/2/16 13:31
 * @Version 1.0
 **/
public interface CourseService {
    /**
     * 生成课程编号
     * 生成策略：随机生成 + 查重
     * 账号规则：专业编号 + 三位数（随机生成）
     *
     * @param majorId 专业编号
     * @return 11个字符的字符串
     */
    String generateCourseId(String majorId);

    /**
     * 获取某个专业下的全部课程
     *
     * @param majorId 专业编号
     * @return 课程集合
     */
    List<Course> queryCourseSetByMajorId(String majorId);

    /**
     * 插入课程信息
     *
     * @param majorId 专业编号
     * @param name    课程姓名
     * @param credit  课程学分
     * @return 成功添加的课程
     */
    Course insertCourse(String majorId, String name, short credit);

    /**
     * 批量插入课程
     *
     * @param courseList 待插入课程列表
     */
    void insertCourseBatch(List<Course> courseList);

    /**
     * 删除课程信息
     *
     * @param courseId
     */
    String deleteCourse(String courseId);

    /**
     * 修改课程信息
     *
     * @param course
     */
    void updateCourse(Course course);

    /**
     * 生成一批课程ID
     *
     * @param majorId    专业编号
     * @param batchCount 数量
     * @return 新课程ID列表
     */
    List<String> generateBatchCourseId(String majorId, int batchCount);

    /**
     * 向学生添加单个课程
     *
     * @param eduId    教育Id
     * @param courseId 课程Id
     */
    void insertCourseByStu(String eduId, String courseId);

    /**
     * 对学生删除单个课程
     *
     * @param eduId    教育Id
     * @param courseId 课程Id
     */
    void deleteCourseByStu(String eduId, String courseId);
}
