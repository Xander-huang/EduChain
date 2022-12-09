package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.dto.IntKvDTO;
import top.zy68.fbChainService.entity.Major;

import java.util.List;
import java.util.Set;

/**
 * @ClassName MajorDao
 * @Description 专业DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface MajorDao {


    /**
     * 通过联盟点编号查询所有专业的编号
     *
     * @param nodeId 联盟点编号
     * @return 专业编号
     */
    Set<String> queryAllMajorIdByNodeId(Integer nodeId);

    /**
     * 通过专业编号查询专业
     *
     * @param majorId 专业编号
     * @return 专业对象
     */
    Major queryMajorById(String majorId);

    /**
     * 通过联盟点编号专业名称和专业类型查询专业
     *
     * @param nodeId    联盟点编号
     * @param majorName 专业编号
     * @param majorType 专业类型
     * @return 专业对象
     */
    Major queryMajorByNameAndType(@Param("nodeId") Integer nodeId, @Param("name") String majorName, @Param("type") String majorType);

    /**
     * 插入一条专业记录
     *
     * @param major 专业对象
     * @return 影响行数
     */
    int insertMajor(Major major);

    /**
     * 通过联盟点编号查询专业
     *
     * @param nodeId 联盟点编号
     * @return 专业列表
     */
    List<Major> queryAllByNodeId(Integer nodeId);

    /**
     * 通过专业编号删除专业
     *
     * @param majorId 专业编号
     * @return 影响的行数
     */
    int deleteByMajorId(String majorId);

    /**
     * 批量插入
     *
     * @param majorList 新专业列表
     * @return 影响行数
     */
    int insertBatch(List<Major> majorList);

    /**
     * 通过联盟点编号和类型查询专业
     *
     * @param nodeId    联盟点编号
     * @param majorType 专业类型
     * @return 专业对象列表
     */
    List<Major> queryMajorsByType(@Param("nodeId") Integer nodeId, @Param("type") String majorType);

    /**
     * 统计联盟点内专业数量
     *
     * @param nodeId 联盟点编号
     * @return 专业数
     */
    Integer countNodeMajor(Integer nodeId);

    /**
     * 统计联盟内每年新开设专业数
     *
     * @param nodeId 联盟点编号
     * @return 每年新专业数列表
     */
    List<IntKvDTO> countAnnualNewMajor(Integer nodeId);

    /**
     * 新增专业毕业学分
     *
     * @param majorId 专业编号
     * @param credit  学分
     * @return 影响行数
     */
    int plusMajorCredit(@Param("majorId") String majorId, @Param("credit") Integer credit);

    /**
     * 更改专业
     *
     * @param majorId        专业编号
     * @param newName        专业名称
     * @param graduateCredit 学分
     * @return 影响行数
     */
    int update(@Param("majorId") String majorId, @Param("newName") String newName, @Param("graduateCredit") Integer graduateCredit);
}

