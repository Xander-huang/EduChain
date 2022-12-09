package top.zy68.personservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.fbChainService.dao.AchieveDao;
import top.zy68.fbChainService.dao.EduRecordDao;
import top.zy68.fbChainService.entity.Achieve;
import top.zy68.fbChainService.entity.EduRecord;
import top.zy68.personservice.exception.ClientBusinessException;
import top.zy68.personservice.exception.NormalBusinessException;
import top.zy68.personservice.service.AchieveService;
import top.zy68.personservice.utils.DateUtil;
import top.zy68.personservice.vo.AchieveRecordVO;
import top.zy68.personservice.vo.AchieveVO;
import top.zy68.personservice.vo.EduAchieveDataVO;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 成就记录(AchievementRecord)表服务实现类
 *
 * @author Sustart
 * @since 2022-02-16 19:29:24
 */
@Slf4j
@Service("achieveService")
public class AchieveServiceImpl implements AchieveService {
    @Resource
    AchieveDao achieveDao;
    @Resource
    EduRecordDao eduRecordDao;
    @Value("${file.store.path}")
    private String filePath;

    /**
     * 通过身份证号查询所有成就数据。
     * 通过身份证号查询所有教育记录，再通过教育记录id查询对应的成就数据。
     *
     * @param personId 身份证号
     * @return 所有的成就记录
     */
    @Override
    public List<AchieveRecordVO> queryAllAchieveRecordByPersonId(String personId) {
        List<EduRecord> eduRecords = eduRecordDao.queryAllByPersonId(personId);
        if (ObjectUtils.isEmpty(eduRecords)) {
            throw new NormalBusinessException("该用户未存在教育记录");
        }

        List<AchieveRecordVO> achieveRecords = new LinkedList<>();
        // 搜索所有的教育经历下 对应的所有的成就
        for (EduRecord eduRecord : eduRecords) {
            List<Achieve> intactRecordList = achieveDao.queryAllByEduId(eduRecord.getEduId());
            if (ObjectUtils.isEmpty(intactRecordList)) {
                // 当前教育经历下没有成就
                continue;
            }

            for (Achieve intactRecord : intactRecordList) {
                AchieveRecordVO achieveVO = new AchieveRecordVO()
                        .setId(intactRecord.getId())
                        .setTitle(intactRecord.getTitle())
                        .setCreateTime(DateUtil.dateToString(intactRecord.getCreateTime(), DateUtil.PATTERN_DATE))
                        .setType(intactRecord.getType())
                        .setAuditStatus(intactRecord.getAuditStatus())
                        .setFeedback(intactRecord.getFeedback())
                        .setCertifyUri(intactRecord.getCertifyUri());
                achieveRecords.add(achieveVO);
            }
        }

        return achieveRecords;
    }

    /**
     * 上传成就证明文件
     *
     * @param files 文件列表
     * @return 新文件名
     */
    @Override
    public String uploadFile(MultipartFile[] files) {
        StringBuilder filesId = new StringBuilder();
        int counter = 0;

        for (MultipartFile file : files) {
            if (ObjectUtils.isEmpty(file)) {
                log.info("文件不存在");
                continue;
            }

            String originalFilename = file.getOriginalFilename();
            log.info("received file: " + originalFilename);
            if (ObjectUtils.isEmpty(originalFilename)) {
                log.info("文件名不合法");
                continue;
            }

            String mD5fileName;
            try {
                // 文件指纹作为文件名
                mD5fileName = DigestUtils.md5DigestAsHex(file.getBytes());
            } catch (IOException e) {
                mD5fileName = UUID.randomUUID().toString().replaceAll("-", "");
                log.warn("文件计算MD5指纹出现异常，已使用UUID替代.");
                e.printStackTrace();
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            // 新文件名 = 文件指纹编码 + 文件扩展名
            String newFileName = mD5fileName + fileExtension;

            File dest = new File(filePath + File.separator + newFileName);
            dest.getParentFile().mkdirs();

            try {
                file.transferTo(dest);
            } catch (Exception e) {
                log.error("文件上传失败{}.", e.getMessage());
                continue;
            }

            log.info("文件: \"{}\" 上传成功.", originalFilename);
            filesId.append(newFileName).append(';');
            counter++;
        }

        log.info("成功上传 {} 个证明文件", counter);
        return filesId.toString();
    }

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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertAchieve(String eduId, String title, String type, Date acquireDate, String remark, String fileIds) {
        // 0 - 认证未通过；1 - 认证已通过; 2 - 审核状态（提交后默认）
        Achieve achieve = new Achieve()
                .setEduId(eduId)
                .setTitle(title)
                .setType(type)
                .setAcquireTime(acquireDate)
                .setRemark(remark)
                .setCertifyUri(fileIds)
                .setAuditStatus((short) 2);

        try {
            this.achieveDao.insertAnAchieve(achieve);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.warn("添加一条成就记录失败：" + e.getMessage());
            throw new ClientBusinessException("添加失败，请检查内容是否有误.");
        }
        log.warn("成功添加一条成就记录.");
    }

    /**
     * 查询用户已经通过的所有成就
     *
     * @param personId 身份证号
     * @return 成就视图列表
     */
    @Override
    public List<EduAchieveDataVO> queryPassedAchieve(String personId) {
        List<EduRecord> eduRecords = eduRecordDao.queryAllByPersonId(personId);
        if (ObjectUtils.isEmpty(eduRecords)) {
            throw new NormalBusinessException("该用户未存在教育记录");
        }
        List<EduAchieveDataVO> resList = new ArrayList<>(eduRecords.size());

        for (EduRecord eduRecord : eduRecords) {
            EduAchieveDataVO eduAchieveDataVO = new EduAchieveDataVO();
            List<Achieve> achieveList = achieveDao.queryPassedByEduId(eduRecord.getEduId());
            if (ObjectUtils.isEmpty(achieveList)) {
                // 当前经历经历下没有已通过的成就
                continue;
            }

            List<AchieveVO> achieveVOList = new ArrayList<>(achieveList.size());
            for (Achieve achieve : achieveList) {
                achieveVOList.add(new AchieveVO(achieve.getId(), achieve.getTitle(), DateUtil.dateToString(achieve.getAcquireTime(), DateUtil.PATTERN_DATE), achieve.getType()));
            }
            eduAchieveDataVO.setEduId(eduRecord.getEduId());
            eduAchieveDataVO.setAchieve(achieveVOList);
            resList.add(eduAchieveDataVO);
        }

        return resList;
    }
}
