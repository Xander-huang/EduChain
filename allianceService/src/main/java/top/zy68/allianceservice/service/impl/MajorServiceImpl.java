package top.zy68.allianceservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.exception.InternalBusinessException;
import top.zy68.allianceservice.exception.NormalBusinessException;
import top.zy68.allianceservice.pojo.vo.MajorVO;
import top.zy68.allianceservice.service.MajorService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.fbChainService.dao.CourseDao;
import top.zy68.fbChainService.dao.MajorDao;
import top.zy68.fbChainService.dao.StudentDao;
import top.zy68.fbChainService.entity.Major;

import javax.annotation.Resource;
import java.util.*;


/**
 * @ClassName MajorServiceImpl
 * @Description 专业服务
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("majorService")
public class MajorServiceImpl implements MajorService {
    @Resource
    MajorDao majorDao;
    @Resource
    StudentDao studentDao;
    @Resource
    CourseDao courseDao;

    /**
     * 查询所有专业
     *
     * @param nodeId 联盟点编号
     * @return 专业列表
     */
    @Override
    public List<MajorVO> queryAllMajors(Integer nodeId) {
        List<Major> majorList = majorDao.queryAllByNodeId(nodeId);
        List<MajorVO> majorVOList = new ArrayList<>(majorList.size());
        for (Major major : majorList) {
            majorVOList.add(new MajorVO(major.getMajorId(), major.getName(), major.getType()));
        }
        return majorVOList;
    }

    /**
     * 查询联盟点内类型为Type的所有专业
     *
     * @param nodeId    联盟点编号
     * @param majorType 专业类型
     * @return 专业列表
     */
    @Override
    public List<MajorVO> getMajorsByType(Integer nodeId, String majorType) {
        String type = null;
        int typeId = 0;
        try {
            typeId = Integer.parseInt(majorType);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new ClientBusinessException("请输入专业类型编码0/1/2/3.");
        }
        if (typeId == 0) {
            type = "本科";
        } else if (typeId == 1) {
            type = "硕士";
        } else if (typeId == 3) {
            type = "职业教育";
        } else if (typeId == 2) {
            type = "博士";
        } else {
            throw new ClientBusinessException("请输入专业类型编码:0/1/2/3.");
        }

        List<Major> majorList = majorDao.queryMajorsByType(nodeId, type);
        if (ObjectUtils.isEmpty(majorList)) {
            throw new NormalBusinessException("不存在该专业类型的专业.");
        }
        List<MajorVO> majorVOList = new ArrayList<>(majorList.size());
        for (Major major : majorList) {
            majorVOList.add(new MajorVO(major.getMajorId(), major.getName(), major.getType()));
        }
        return majorVOList;
    }

    /**
     * 添加一个专业
     *
     * @param nodeId    联盟点编号
     * @param majorName 专业
     * @param majorType 专业所属类型
     */
    @Override
    public MajorVO addMajor(Integer nodeId, String majorName, String majorType) {
        Major isExist = majorDao.queryMajorByNameAndType(nodeId, majorName, majorType);
        if (!Objects.isNull(isExist)) {
            throw new ClientBusinessException("该类型的专业已存在.");
        }
        // 生成专业编号
        String majorId = generateMajorId(nodeId);
        Major major = new Major()
                .setMajorId(majorId)
                .setName(majorName)
                .setType(majorType)
                .setGraduateCredit(0);

        try {
            majorDao.insertMajor(major);
        } catch (Exception e) {
            log.warn("新增专业失败.");
            throw new InternalBusinessException("添加专业失败.");
        }

        log.info("专业添加成功");
        return new MajorVO(major.getMajorId(), major.getName(), major.getType());
    }

    /**
     * 生成专业编号
     * 生成策略：随机生成 + 查重
     * 账号规则：联盟点编号 + 三位数（随机生成）
     *
     * @param nodeId 联盟点编号
     * @return 8个字符的字符串
     */
    @Override
    public String generateMajorId(Integer nodeId) {
        // 默认生成的账号最多重复1000次
        int generateTime = 1000;
        String majorId = null;
        Set<String> majorIdSet = majorDao.queryAllMajorIdByNodeId(nodeId);
        for (int i = 0; i < generateTime; i++) {
            majorId = nodeId + IdUtil.generateRandomNumber(IdUtil.MAJOR_ID);
            // 如果set中没有该id则不重复，直接取用
            if (!majorIdSet.contains(majorId) && majorId.length() == 8) {
                break;
            }
        }
        return majorId;
    }

    /**
     * 删除专业
     *
     * @param majorId 专业编号
     */
    @Override
    public void delMajor(String majorId) {
        // 检测是否存在该专业学生
        int stuNum = studentDao.countMajorStu(majorId);
        if (stuNum != 0) {
            throw new ClientBusinessException("当前专业存在学生，不可删除.");
        }

        try {
            majorDao.deleteByMajorId(majorId);
            courseDao.deleteAllCourseByMajorId(majorId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("专业删除失败");
            throw new InternalBusinessException("专业删除失败");
        }
    }

    /**
     * 批量插入专业
     *
     * @param majorList 待插入专业列表
     */
    @Override
    public void insertMajorBatch(List<Major> majorList) {
        try {
            int row = majorDao.insertBatch(majorList);
            log.info("成功插入{}条数据", row);
        } catch (Exception e) {
            log.warn("批量导入专业异常");
            e.printStackTrace();
        }
    }

    /**
     * 生成一批专业ID
     *
     * @param nodeId     联盟点编号
     * @param batchCount 数量
     * @return 新专业ID列表
     */
    @Override
    public List<String> generateBatchMajorId(Integer nodeId, int batchCount) {
        Set<String> majorIdSet = new HashSet<>(batchCount);
        while (majorIdSet.size() < batchCount) {
            String id = generateMajorId(nodeId);
            majorIdSet.add(id);
        }
        return new ArrayList<>(majorIdSet);
    }
}
