package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.dto.IntKvDTO;
import top.zy68.fbChainService.entity.Course;


import java.util.List;
import java.util.Set;

/**
 * @ClassName CourseDao
 * @Description 课程DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface CourseDao {

    /**
     * 通过课程编号查询课程
     *
     * @param courseId 课程编号
     * @return 课程对象
     */
    Course queryByCourseId(String courseId);

    /**
     * 通过专业编号查询所有课程编号
     *
     * @param majorId 专业编号
     * @return 所有课程编号
     */
    Set<String> queryAllCourseIdByMajorId(String majorId);

    /**
     * 批量插入课程
     *
     * @param courseList 课程列表
     * @return 影响行数
     */
    int insertBatch(List<Course> courseList);

    /**
     * 通过专业编号查询所属专业下全部课程
     *
     * @param majorId
     * @return
     */
    List<Course> queryCourseSetByMajorId(String majorId);


    /**
     * 添加课程
     *
     * @param course
     * @return
     */
    int insertCourse(Course course);

    /**
     * 批量添加课程
     *
     * @param courses
     * @return
     */
    int insertCourseBatch(@Param("courses") List<Course> courses);

    /**
     * 通过课程编号删除课程
     *
     * @param courseId
     * @return
     */
    int deleteCourse(String courseId);


    /**
     * 修改课程信息
     *
     * @param course
     * @return
     */
    int updateCourse(Course course);

    /**
     * 统计联盟点内课程数量
     *
     * @param nodeId 联盟点编号
     * @return 课程数量
     */
    Integer countNodeCourse(Integer nodeId);

    /**
     * 统计联盟点内每年开设的新课程数量
     *
     * @param nodeId 联盟点编号
     * @return 每年课程数量列表
     */
    List<IntKvDTO> countAnnualNewCourse(Integer nodeId);

    /**
     * 删除专业下的所有课程
     *
     * @param majorId 专业编号
     * @return 影响行数
     */
    int deleteAllCourseByMajorId(String majorId);
}

