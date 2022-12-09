package top.zy68.allianceservice.service;

import top.zy68.allianceservice.pojo.vo.AchieveVO;
import top.zy68.fbChainService.entity.Achieve;

import java.util.Date;
import java.util.List;

/**
 * @ClassName AchieveRecordService
 * @Description 成就记录管理服务
 * @Author Sustart
 * @Date2022/2/12 13:31
 * @Version 1.0
 **/
public interface AchieveRecordService {

    /**
     * 查询所有学生的所有成就
     *
     * @param nodeNumber 联盟点编号
     * @return 成就列表
     */
    List<AchieveVO> queryAllAchieves(Integer nodeNumber);

    /**
     * 修改认证成就状态
     *
     * @param id          成就编号
     * @param auditStatus 审批状态
     * @param feedback    反馈信息
     */
    void updateAchievesStatus(Integer id, Short auditStatus, String feedback);


    /**
     * 联盟用户添加学生成就
     *
     * @param eduId       学号
     * @param title       标题
     * @param type        类型
     * @param acquireTime 获得时间
     * @return 添加成功的成就
     */
    Achieve insertAchieves(String eduId, String title, String type, Date acquireTime);
}
