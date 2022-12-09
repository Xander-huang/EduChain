package top.zy68.personservice.service;

import org.springframework.web.multipart.MultipartFile;
import top.zy68.personservice.vo.AchieveRecordVO;
import top.zy68.personservice.vo.EduAchieveDataVO;

import java.util.Date;
import java.util.List;

/**
 * 成就记录(AchievementRecord)表服务接口
 *
 * @author Sustart
 * @since 2022-02-16 19:29:24
 */
public interface AchieveService {
    /**
     * 上传成就证明文件
     *
     * @param files 文件列表
     * @return 新文件名
     */
    String uploadFile(MultipartFile[] files);

    /**
     * 通过身份证号查询所有成就数据
     *
     * @param personId 身份证号
     * @return 所有的成就记录
     */
    List<AchieveRecordVO> queryAllAchieveRecordByPersonId(String personId);

    /**
     * 增加一个成就记录
     *
     * @param eduId       教育经历ID
     * @param title       记录标题
     * @param type        记录类型
     * @param acquireDate 获得日期
     * @param remark      备注
     * @param fileIds     文件id
     */
    void insertAchieve(String eduId, String title, String type, Date acquireDate, String remark, String fileIds);

    /**
     * 查询用户已经通过的所有成就
     *
     * @param personId 身份证号
     * @return 成就视图列表
     */
    List<EduAchieveDataVO> queryPassedAchieve(String personId);
}
