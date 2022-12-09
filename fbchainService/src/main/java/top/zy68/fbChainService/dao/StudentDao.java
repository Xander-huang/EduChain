package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.entity.Student;
import java.util.List;

/**
 * @ClassName StudentDao
 * @Description 学生信息DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface StudentDao {
    /**
     * 插入一个学生
     *
     * @param student 学生对象
     * @return 影响行数
     */
    int insertStudent(Student student);

    /**
     * 批量插入学生
     *
     * @param studentList 实体对象
     * @return 影响行数
     */
    int insertStudentBatch(List<Student> studentList);

    /**
     * 通过学号删除学生
     *
     * @param stuId 学号
     * @return 影响行数
     */
    int deleteStudentByStuId(String stuId);

    /**
     * 通过学号查询学生信息
     *
     * @param stuId 学号
     * @return 学生对象
     */
    Student queryByStuId(String stuId);

    /**
     * 通过学号更新一条学生记录
     *
     * @param stuId   学号
     * @param student 学生对象
     * @return 影响行数
     */
    int updateByStuId(@Param("stuId") String stuId, @Param("student") Student student);

    /**
     * 查询联盟点内所有学生
     *
     * @param nodeId 联盟点编号
     * @return 学生列表
     */
    List<Student> queryAllByNodeId(Integer nodeId);

    /**
     * 统计联盟点内的学生总数
     *
     * @param nodeId 联盟点编号
     * @return 学生数
     */
    Integer countNodeStu(Integer nodeId);

    /**
     * 统计联盟内已经上链的学生数量
     *
     * @param nodeId 联盟点编号
     * @return 已上链学生数
     */
    Integer countNodeUplinkedStu(Integer nodeId);

    /**
     * 根据专业编号统计该专业的学生数量
     *
     * @param majorId 专业编号
     * @return 学生对象
     */
    Integer countMajorStu(String majorId);

    /**
     * 通过身份证号和专业编号查询学生
     *
     * @param personId 身份证号
     * @param majorId  专业编号
     * @return 学生
     */
    Student queryStuByPersonIdAndMajorId(@Param("personId") String personId, @Param("majorId") String majorId);
}

