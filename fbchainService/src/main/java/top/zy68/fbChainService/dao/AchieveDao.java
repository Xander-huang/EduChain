package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.entity.Achieve;


import java.util.List;

/**
 * @ClassName AchieveDao
 * @Description 成就记录DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface AchieveDao {
    /**
     * 查询当前教育经历的所有成就
     *
     * @param eduId 教育经历id
     * @return 成就列表
     */
    List<Achieve> queryAllByEduId(String eduId);

    /**
     * 插入一条成就
     *
     * @param achieve 成就对象
     * @return 影响行数
     */
    int insertAnAchieve(Achieve achieve);

    /**
     * 更改教育经历id
     *
     * @param oldEduId 旧教育经历id
     * @param newEduId 新教育经历id
     * @return 影响行数
     */
    int updateEduId(@Param("oldEduId") String oldEduId, @Param("newEduId") String newEduId);

    /**
     * 根据id修改成就信息
     *
     * @param achieve
     * @return
     */
    int updateAchieve(Achieve achieve);


    /**
     * 根据成就id查询成就信息
     *
     * @param id 成就id
     * @return
     */
    Achieve queryById(Integer id);

    /**
     * 统计教育经历下的成就
     *
     * @param eduId 教育经历ID
     * @return 成就数量
     */
    int countEduAchieve(String eduId);

    /**
     * 查询教育经历下所有已审批通过的成就
     *
     * @param eduId 教育经历ID
     * @return 成就列表
     */
    List<Achieve> queryPassedByEduId(String eduId);

    /**
     * 删除教育经历ID下的所有成就信息
     *
     * @param eduId 教育经历ID
     * @return 影响行数
     */
    int deleteAchieveByEduId(String eduId);
}

