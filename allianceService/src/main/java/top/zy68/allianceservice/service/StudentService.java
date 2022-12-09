package top.zy68.allianceservice.service;

import top.zy68.allianceservice.pojo.vo.EduVO;
import top.zy68.allianceservice.pojo.vo.StudentInfoVO;
import top.zy68.fbChainService.entity.Major;
import top.zy68.fbChainService.entity.Student;

import java.util.List;
import java.util.Map;

/**
 * @ClassName StudentService
 * @Description 学生服务
 * @Author Sustart
 * @Date2022/2/16 13:31
 * @Version 1.0
 **/
public interface StudentService {

    /**
     * 生成学生编号
     * 账号规则：专业编号 + UUID
     * 生成策略：UUID生成
     *
     * @param majorId 专业编号
     * @return 40个字符的字符串
     */
    String generateStuId(String majorId);

    /**
     * 添加一个学生
     *
     * @param personId 身份证号
     * @param majorId  专业编号
     * @return 新增学生的信息
     */
    StudentInfoVO addStudent(String personId, String majorId);

    /**
     * 删除一个学生
     * 实际是删除该学号所对应的所有数据
     *
     * @param stuId 学号
     * @return 被删除学生的学号
     */
    String deleteStudent(String stuId);

    /**
     * 批量添加学生
     *
     * @param studentList    新学生列表
     * @param nodeId         机构编号
     * @param cachedMajorMap 专业编号和专业对象
     */
    void insertStudentBatch(List<Student> studentList, int nodeId, Map<String, Major> cachedMajorMap);

    /**
     * 更改学生专业
     * 注：清除旧专业课程、生成新学号（更改专业编号段）、更改所有内容中的旧学号为新学号
     *
     * @param stuId   学生
     * @param majorId 新专业编号
     * @return 新旧学号
     */
    Map<String, String> changeStudentMajor(String stuId, String majorId);

    /**
     * 获取联盟点内所有学生
     *
     * @param nodeId 联盟点编号
     * @return 学生信息列表
     */
    List<StudentInfoVO> getAllStudents(Integer nodeId);

    /**
     * 获取联盟点内所有的专业信息，封装为 专业编号 - 专业对象 的形式
     *
     * @param nodeId 联盟点编号
     * @return map
     */
    Map<String, Major> getMajorKeyValueMap(Integer nodeId);

    /**
     * 批量上链学生教育记录
     *
     * @param stuIdList 上链学生学号
     * @return 成功上链学生ID列表
     */
    List<String> uplinkStuEduInfo(String[] stuIdList);

    /**
     * 获取学生在联盟内教育id
     *
     * @param personId 身份证号
     * @return 教育信息列表
     */
    List<EduVO> getStudentIdAndMajor(String personId);
}
