package top.zy68.personservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.zy68.fbChainService.model.chainMainInfo.BlockMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.EduRecordMainInfo;
import top.zy68.personservice.pojo.ChainAchieve;
import top.zy68.personservice.pojo.ChainCourse;

import java.math.BigInteger;
import java.util.List;

/**
 * @ClassName EducationVO
 * @Description 教育记录信息封装
 * @Author Sustart
 * @Date2022/4/9 19:42
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChainEducationVO {
    private String eduId;

    private String personId;

    private String eduType;

    private BigInteger nodeId;

    private String nodeName;

    private String beginTime;

    private String endTime;

    private String majorName;

    private BigInteger acquireCredit;

    private BigInteger graduateCredit;

    private String certifyFile;
    /**
     * 区块产生时间
     */
    private String createTime;
    /**
     * 交易哈希
     */
    private String transactionHash;
    /**
     * 交易序号
     */
    private String transactionIndex;
    /**
     * 区块哈希
     */
    private String blockHash;
    /**
     * 区块号
     */
    private String blockNumber;

    private List<ChainCourse> course;
    private List<ChainAchieve> achieve;

    /**
     * 定义个构造方法
     *
     * @param eduRecordMainInfo 上链的教育主要信息
     * @param blockMainInfo     区块信息
     * @param course            上链课程列表
     * @param achieve           上链成就列表
     */
    public ChainEducationVO(EduRecordMainInfo eduRecordMainInfo, BlockMainInfo blockMainInfo, List<ChainCourse> course, List<ChainAchieve> achieve) {
        this.eduId = eduRecordMainInfo.getEduId();
        this.personId = eduRecordMainInfo.getPersonId();
        this.eduType = eduRecordMainInfo.getEduType();
        this.nodeId = eduRecordMainInfo.getNodeId();
        this.nodeName = eduRecordMainInfo.getNodeName();
        this.beginTime = eduRecordMainInfo.getBeginTime();
        this.endTime = eduRecordMainInfo.getEndTime();
        this.majorName = eduRecordMainInfo.getMajorName();
        this.acquireCredit = eduRecordMainInfo.getAcquireCredit();
        this.graduateCredit = eduRecordMainInfo.getGraduateCredit();
        this.certifyFile = eduRecordMainInfo.getCertifyFile();

        this.createTime = blockMainInfo.getCreateTime();
        this.transactionHash = blockMainInfo.getTransactionHash();
        this.transactionIndex = blockMainInfo.getTransactionIndex();
        this.blockHash = blockMainInfo.getBlockHash();
        this.blockNumber = blockMainInfo.getBlockNumber();

        this.course = course;
        this.achieve = achieve;
    }

}
