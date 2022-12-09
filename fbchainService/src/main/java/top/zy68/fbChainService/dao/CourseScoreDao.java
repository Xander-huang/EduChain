package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.dto.ScoreDTO;
import top.zy68.fbChainService.dto.StrFloatKvDTO;
import top.zy68.fbChainService.entity.CourseScore;


import java.util.List;

/**
 * @ClassName CourseScoreDao
 * @Description 课程成绩DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface CourseScoreDao {
    /**
     * 通过教育记录id查询所学的所有课程成绩
     *
     * @param eduId 教育记录id
     * @return 课程成绩列表
     */
    List<CourseScore> queryAllByEduId(String eduId);

    /**
     * 通过学号删除该学生所参加的所有课程
     *
     * @param eduId 学号
     * @return 影响行数
     */
    int deleteAllByEduId(String eduId);

    /**
     * 向学生添加单个课程
     *
     * @param eduId
     * @param courseId
     * @return
     */
    int insertCourseByStu(@Param("eduId") String eduId, @Param("courseId") String courseId);

    /**
     * 批量插入课程成绩
     *
     * @param scoreList 成绩列表
     * @return 插入行数
     */
    int insertCourseScoreBatch(List<ScoreDTO> scoreList);

    /**
     * 向学生删除单个课程
     *
     * @param eduId
     * @param courseId
     * @return
     */
    int deleteCourseByStu(@Param("eduId") String eduId, @Param("courseId") String courseId);

    /**
     * 通过教育记录id查询所学的所有课程成绩(含课程信息)
     *
     * @param eduId 教育记录id
     * @return 课程成绩列表(含课程信息)
     */
    List<CourseScore> queryAllWithCourseByEduId(String eduId);

    /**
     * 查询联盟点内当前年份专业课程平均分排名前 priorityMajorNum 的专业编号及分数
     *
     * @param currYear         当前年份
     * @param nodeId           联盟点编号
     * @param priorityMajorNum 专业数
     * @return 专业编号及分数列表
     */
    List<StrFloatKvDTO> queryMajorIdAndAvgCourseScore(@Param("currYear") Integer currYear, @Param("nodeId") Integer nodeId, @Param("priorityMajorNum") Integer priorityMajorNum);

    /**
     * 计算联盟点内当前年平均分在区间范围内的学生的数量
     *
     * @param currYear 年份
     * @param nodeId   联盟点编号
     * @param big      大数边界
     * @param small    小数边界
     * @return 学生数量
     */
    int countStuNumByAvgScoreInRange(@Param("currYear") Integer currYear, @Param("nodeId") Integer nodeId, @Param("big") Integer big, @Param("small") Integer small);

    /**
     * 计算联盟点内当前年平均分小于指定分数的学生的数量
     *
     * @param currYear 年份
     * @param nodeId   联盟点编号
     * @param score    分数
     * @return 学生数量
     */
    int countStuNumByAvgScoreLessThan(@Param("currYear") Integer currYear, @Param("nodeId") Integer nodeId, @Param("score") Integer score);

    /**
     * 根据id修改学生课程成绩
     *
     * @param courseScore
     * @return
     */
    int updateById(CourseScore courseScore);

    /**
     * 通过学号和课号更新学生课程成绩
     *
     * @param eduId    学号
     * @param courseId 课程编号
     * @param score    分数
     * @return 影响行数
     */
    int updateScoreByStuIdAndCourseId(@Param("eduId") String eduId, @Param("courseId") String courseId, @Param("score") Integer score);

    /**
     * 插入学生课程+成绩
     *
     * @param eduId    学号
     * @param courseId 课号
     * @param score    分数
     * @return 影响行数
     */
    int insertCourseScore(@Param("eduId") String eduId, @Param("courseId") String courseId, @Param("score") Integer score);

    /**
     * 查询一个学生一门课程成绩
     *
     * @param eduId    学号
     * @param courseId 课程编号
     * @return 课程成绩
     */
    CourseScore queryWithCourseByStuIdAndCourseId(@Param("eduId") String eduId, @Param("courseId") String courseId);
}

