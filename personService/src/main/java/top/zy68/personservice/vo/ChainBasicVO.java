package top.zy68.personservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.zy68.fbChainService.model.chainMainInfo.BlockMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;


/**
 * @ClassName ChainBasicVO
 * @Description 封装个人基本信息的视图
 * @Author Sustart
 * @Date2022/4/10 11:04
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChainBasicVO {
    /**
     * 身份证号
     */
    private String personId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 民族
     */
    private String nation;
    /**
     * 出生日期
     */
    private String birthday;

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

    /**
     * 通过两个主要信息对象设置属性
     *
     * @param personMainInfo 上链的个人主要信息
     * @param blockMainInfo  区块信息
     */
    public ChainBasicVO(PersonMainInfo personMainInfo, BlockMainInfo blockMainInfo) {
        this.personId = personMainInfo.getPersonId();
        this.name = personMainInfo.getName();
        this.gender = personMainInfo.getGender();
        this.nation = personMainInfo.getNation();
        this.birthday = personMainInfo.getBirthday();

        this.createTime = blockMainInfo.getCreateTime();
        this.transactionHash = blockMainInfo.getTransactionHash();
        this.transactionIndex = blockMainInfo.getTransactionIndex();
        this.blockHash = blockMainInfo.getBlockHash();
        this.blockNumber = blockMainInfo.getBlockNumber();
    }
}
