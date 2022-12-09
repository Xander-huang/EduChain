package top.zy68.allianceservice.service;

import top.zy68.allianceservice.pojo.dto.NodeRegisterDTO;
import top.zy68.allianceservice.pojo.vo.NodeDataVO;

/**
 * @ClassName AllianceNodeService
 * @Description 联盟点服务
 * @Author Sustart
 * @Date2022/2/16 13:31
 * @Version 1.0
 **/
public interface AllianceNodeService {
    /**
     * 获取节点相关数据
     *
     * @param nodeId 联盟点编号
     * @return 节点数据封装视图对象
     */
    NodeDataVO getNodeData(Integer nodeId);

    /**
     * 注册一个机构及其管理员的通知
     *
     * @param registerDTO 注册DTO
     * @return 注册返回消息
     */
    String registerNodeNotification(NodeRegisterDTO registerDTO);

    /**
     * 审批机构注册
     *
     * @param key      redis key
     * @param auditRes 结果
     * @return 审批结果
     */
    String auditRegister(String key, String auditRes);
}
