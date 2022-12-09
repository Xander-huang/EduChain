package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.exception.InternalBusinessException;
import top.zy68.allianceservice.service.EduRecordService;
import top.zy68.fbChainService.dao.EduRecordDao;
import top.zy68.fbChainService.entity.EduRecord;

import javax.annotation.Resource;


/**
 * @ClassName EduRecordServiceImpl
 * @Description 教育记录服务
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("eduRecordService")
public class EduRecordServiceImpl implements EduRecordService {
    @Resource
    private EduRecordDao eduRecordDao;

    /**
     * 通过三个参数确定教育经历ID
     *
     * @param personId 身份证号
     * @param eduType  教育类型
     * @param majorId  专业编号
     * @return 教育经历ID
     */
    @Override
    public String queryEduIdByCondition(String personId, String eduType, String majorId) {
        return eduRecordDao.queryEduIdByCondition(personId, eduType, majorId);
    }

    /**
     * 给教育经历添加证书文件
     *
     * @param eduId  教育经历ID
     * @param fileId 文件名称
     */
    @Override
    public void updateEduRecordCertifyFile(String eduId, String fileId) {
        EduRecord eduRecord = eduRecordDao.queryByEduId(eduId);
        if (ObjectUtils.isEmpty(eduRecord.getCertifyUri())) {
            eduRecord.setCertifyUri(fileId);
        } else {
            eduRecord.setCertifyUri(fileId + ";" + eduRecord.getCertifyUri());
        }
        try {
            eduRecordDao.updateByEduId(eduId, eduRecord);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalBusinessException("文件上传失败.");
        }
    }
}
