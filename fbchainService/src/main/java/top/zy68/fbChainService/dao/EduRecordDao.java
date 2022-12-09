package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.dto.IntKvDTO;
import top.zy68.fbChainService.entity.EduRecord;


import java.util.List;

/**
 * @ClassName EduRecordDao
 * @Description 教育记录DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface EduRecordDao {
    /**
     * 通过身份证号查询所有教育记录
     *
     * @param personId 身份证号
     * @return 教育记录列表
     */
    List<EduRecord> queryAllByPersonId(String personId);

    /**
     * 插入一条教育记录
     *
     * @param eduRecord 教育记录对象
     * @return 影响行数
     */
    int insert(EduRecord eduRecord);

    /**
     * 通过教育经历Id查询一条教育经历记录
     *
     * @param eduId 教育经历ID
     * @return 教育经历记录
     */
    EduRecord queryByEduId(String eduId);

    /**
     * 通过教育经历Id更新教育记录
     *
     * @param eduId     教育经历id
     * @param eduRecord 新的教育经历数据对象
     * @return 影响行数
     */
    int updateByEduId(@Param("eduId") String eduId, @Param("eduRecord") EduRecord eduRecord);

    /**
     * 统计联盟点内每年新入学学生数
     *
     * @param nodeId 联盟点编号
     * @return 每年学生数列表
     */
    List<IntKvDTO> countAnnualNewStu(@Param("nodeId") Integer nodeId);

    /**
     * 统计联盟点内每年上链学生数
     *
     * @param nodeId 联盟点编号
     * @return 每年上链学生数量
     */
    List<IntKvDTO> countAnnualUplinkedStu(Integer nodeId);

    /**
     * 统计当前年份，联盟内新增学生总数前 priorityMajorNum 个专业及学生数
     *
     * @param currYear         年份
     * @param nodeId           联盟点编号
     * @param priorityMajorNum 专业数
     * @return 专业编号及其学生数
     */
    List<IntKvDTO> countCurrYearMajorStuNum(@Param("currYear") Integer currYear, @Param("nodeId") Integer nodeId, @Param("priorityMajorNum") Integer priorityMajorNum);

    /**
     * 通过三个参数确定教育记录ID
     *
     * @param personId 身份证号
     * @param eduType  教育类型
     * @param majorId  专业编号
     * @return 教育经历ID
     */
    String queryEduIdByCondition(@Param("personId") String personId, @Param("eduType") String eduType, @Param("majorId") String majorId);

    /**
     * 根据教育经历ID增加学生已获学分
     *
     * @param eduId  教育经历ID
     * @param credit 新获得学分
     * @return 影响行数
     */
    int plusAcCredit(@Param("eduId") String eduId, @Param("credit") int credit);

    /**
     * 通过教育经历ID删除教育经历
     *
     * @param eduId 教育经历ID
     * @return 影响行数
     */
    int deleteEduRecord(String eduId);

    /**
     * 统计联盟点内已上链学生总数
     *
     * @param nodeId 联盟点编号
     * @return 已上链学生总数
     */
    Integer countNodeUplinkedStuNum(Integer nodeId);
}

