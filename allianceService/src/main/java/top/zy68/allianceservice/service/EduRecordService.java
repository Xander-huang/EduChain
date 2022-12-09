package top.zy68.allianceservice.service;

/**
 * @ClassName EduRecordService
 * @Description 教育记录服务
 * @Author Sustart
 * @Date2022/2/16 13:31
 * @Version 1.0
 **/
public interface EduRecordService {
    /**
     * 通过三个参数确定教育经历ID
     *
     * @param personId 身份证号
     * @param eduType  教育类型
     * @param majorId  专业编号
     * @return 教育经历ID
     */
    String queryEduIdByCondition(String personId, String eduType, String majorId);

    /**
     * 给教育经历添加证书文件
     *
     * @param eduId  教育经历ID
     * @param fileId 文件名称
     */
    void updateEduRecordCertifyFile(String eduId, String fileId);
}
