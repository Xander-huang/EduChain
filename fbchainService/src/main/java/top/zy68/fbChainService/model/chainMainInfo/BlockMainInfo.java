package top.zy68.fbChainService.model.chainMainInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName BlockMainInfo
 * @Description 区块的主要信息
 * @Author Sustart
 * @Date2022/4/6 12:03
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class BlockMainInfo {
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
}
