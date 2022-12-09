package top.zy68.personservice.service;

import top.zy68.personservice.dto.AllianceDTO;
import top.zy68.personservice.vo.ChainDataVO;
import top.zy68.personservice.vo.EduRecordScoreVO;

import java.util.List;

/**
 * 教育记录(EducationRecord)表服务接口
 *
 * @author Sustart
 * @since 2022-02-16 19:29:59
 */
public interface EduService {
    /**
     * 通过身份证号查询用户的所有教育记录（含课程）
     *
     * @param personId 身份证号
     * @return 所有教育记录（含课程）
     */
    List<EduRecordScoreVO> queryEduRecord(String personId);

    /**
     * 通过身份账号查询该用户的所有教育机构
     *
     * @param personId 身份证号
     * @return 多条教育机构信息
     */
    List<AllianceDTO> queryAllAlliance(String personId);

    /**
     * 分享已经上链的数据
     * 将访问key/数据验证码发送到邮箱
     *
     * @param personId  身份证号
     * @param eduIdList 教育经历ID列表
     */
    void shareDataInChain(String personId, List<String> eduIdList);

    /**
     * 给指定邮箱发送数据校验码
     *
     * @param email     邮箱
     * @param accessKey 数据校验码
     */
    void sendEmailAccessKey(String email, String accessKey);

    /**
     * 查询链上数据
     *
     * @param personId 身份证号
     * @return 封装数据
     */
    ChainDataVO getChainData(String personId);





}
