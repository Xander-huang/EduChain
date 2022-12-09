package top.zy68.fbChainService.service;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.zy68.fbChainService.common.ReceiptStatus;
import top.zy68.fbChainService.model.chainMainInfo.AchieveMainInfo;
import top.zy68.fbChainService.raw.AchieveSol;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName AchieveSolService
 * @Description 封装成就合约类的service，对外提供成就合约相关的服务。
 * @Author Sustart
 * @Date2022/04/04 21:44
 * @Version 1.0
 **/

@Service
@Slf4j
public class AchieveSolService {
    @Value("${system.contract.achieveSolAddress}")
    private String address;
    @Resource
    private Client client;

    AchieveSol achieveSol;

    /**
     * 初始化achieveSol
     */
    @PostConstruct
    public void init() {
        achieveSol = new AchieveSol(address, client, client.getCryptoSuite().getCryptoKeyPair());
    }

    /**
     * 通过教育记录id查询成就记录数据
     *
     * @param eduId 教育经历id
     * @return 成就数据列表
     */
    public AchieveMainInfo select(String eduId) {
        Tuple3<List<String>, List<String>, List<String>> selectResponse;
        try {
            selectResponse = achieveSol.select(eduId);
            if (Objects.isNull(selectResponse)) {
                log.warn("未查询到已上链成就记录.");
                return null;
            }
            return new AchieveMainInfo(eduId, selectResponse.getValue1(), selectResponse.getValue2(), selectResponse.getValue3());
        } catch (ContractException e) {
            log.warn("查询成就记录出现异常.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上链并返回交易Hash值
     *
     * @param achieve 上链数据
     * @return 交易Hash
     */
    public String insert(AchieveMainInfo achieve) {
        TransactionReceipt receipt = achieveSol.insert(achieve.getEduId(), achieve.getTitleList(), achieve.getAcquireTimeList(), achieve.getCertifyFileList());
        log.info(receipt.toString());
        String insertRes = receipt.getStatus();
        // 符合NORMAL，上链一定正常。其他状态当上链失败。
        if (Objects.equals(insertRes, ReceiptStatus.NORMAL)) {
            return receipt.getTransactionHash();
        }
        return null;
    }
}
