package top.zy68.fbChainService.service;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BcosBlock;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.springframework.stereotype.Service;
import top.zy68.fbChainService.model.chainMainInfo.BlockMainInfo;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName ClientService
 * @Description 提供区块链信息相关的公共服务
 * @Author Sustart
 * @Date2022/4/6 11:53
 * @Version 1.0
 **/
@Service
@Slf4j
public class ClientService {
    @Resource
    private Client client;

    /**
     * 通过交易哈希获取区块的主要信息
     *
     * @param transactionHash 交易哈希
     * @return 区块主要信息
     */
    public BlockMainInfo getBlockInfoByTransactionHash(String transactionHash) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            TransactionReceipt receipt = client.getTransactionReceipt(transactionHash).getResult();
            BcosBlock blockByHash = client.getBlockByHash(receipt.getBlockHash(), true);
            return new BlockMainInfo(sdf.format(new Date(Long.parseLong(blockByHash.getBlock().getTimestamp().substring(2), 16))), transactionHash, receipt.getTransactionIndex(), receipt.getBlockHash(), receipt.getBlockNumber());
        } catch (Exception e) {
            log.error("查询区块主要信息异常");
            e.printStackTrace();
            return null;
        }
    }
}
