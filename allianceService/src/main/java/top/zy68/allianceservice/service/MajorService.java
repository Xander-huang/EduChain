package top.zy68.allianceservice.service;

import top.zy68.allianceservice.pojo.vo.MajorVO;
import top.zy68.fbChainService.entity.Major;

import java.util.List;

/**
 * @ClassName MajorService
 * @Description 专业服务
 * @Author Sustart
 * @Date2022/2/16 13:31
 * @Version 1.0
 **/
public interface MajorService {
    /**
     * 生成专业编号
     * 生成策略：随机生成 + 查重
     * 账号规则：联盟点编号 + 三位数（随机生成）
     *
     * @param nodeId 联盟点编号
     * @return 8个字符的字符串
     */
    String generateMajorId(Integer nodeId);

    /**
     * 添加一个专业
     *
     * @param nodeId    联盟点编号
     * @param majorName 专业
     * @param majorType 专业所属类型
     */
    MajorVO addMajor(Integer nodeId, String majorName, String majorType);

    /**
     * 删除专业
     *
     * @param majorId 专业编号
     */
    void delMajor(String majorId);

    /**
     * 查询所有专业
     *
     * @param nodeId 用户账号
     * @return 专业列表
     */
    List<MajorVO> queryAllMajors(Integer nodeId);

    /**
     * 批量插入专业
     *
     * @param majorList 待插入专业列表
     */
    void insertMajorBatch(List<Major> majorList);

    /**
     * 生成一批专业ID
     *
     * @param nodeId     联盟点编号
     * @param batchCount 数量
     * @return 新专业ID列表
     */
    List<String> generateBatchMajorId(Integer nodeId, int batchCount);

    /**
     * 查询联盟点内类型为Type的所有专业
     *
     * @param nodeId    联盟点编号
     * @param majorType 专业类型
     * @return 专业列表
     */
    List<MajorVO> getMajorsByType(Integer nodeId, String majorType);
}
