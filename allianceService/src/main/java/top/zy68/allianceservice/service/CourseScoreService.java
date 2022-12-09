package top.zy68.allianceservice.service;

import top.zy68.allianceservice.pojo.UpdateScoreBO;
import top.zy68.allianceservice.pojo.dto.StuCourseScoreDTO;
import top.zy68.allianceservice.pojo.vo.StudentCourseScoreVO;

import java.util.List;

/**
 * @ClassName CourseScoreService
 * @Description 课程成绩服务
 * @Author Sustart
 * @Date2022/2/16 13:31
 * @Version 1.0
 **/
public interface CourseScoreService {

    /**
     * 获取联盟点内的学生及其课程成绩
     *
     * @param nodeId
     * @return
     */
    List<StudentCourseScoreVO> getAllStudentEduRecords(Integer nodeId);

    /**
     * 批量更改课程成绩
     *
     * @param scoreDataList 课程成绩列表
     * @param failedList    导入失败数据列表
     */
    void updateScoreBatch(List<UpdateScoreBO> scoreDataList, List<String> failedList);

    /**
     * 修改一个学生的多个成绩
     *
     * @param stuCourseScoreDTO 成绩传输对象
     */
    void updateOneStudentCourseScore(StuCourseScoreDTO stuCourseScoreDTO);

    /**
     * 给学生课程添加证书
     *
     * @param eduId    教育经历ID
     * @param courseId 课程编号
     * @param fileId   文件名称
     */
    void updateStuCourseCertify(String eduId, String courseId, String fileId);
}
