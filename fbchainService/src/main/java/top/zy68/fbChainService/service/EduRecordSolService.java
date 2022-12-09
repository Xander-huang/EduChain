package top.zy68.fbChainService.service;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple10;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.zy68.fbChainService.common.ReceiptStatus;
import top.zy68.fbChainService.model.chainMainInfo.EduRecordMainInfo;
import top.zy68.fbChainService.raw.EduRecordSol;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Objects;

/**
 * @ClassName EduRecordSolService
 * @Description 封装教育记录合约类的service，对外提供教育记录合约相关的服务。
 * @Author Sustart
 * @Date2022/04/04 21:44
 * @Version 1.0
 **/

@Service
@Slf4j
public class EduRecordSolService {

    @Value("${system.contract.eduRecordSolAddress}")
    private String address;

    @Resource
    private Client client;

    EduRecordSol eduRecordSol;

    /**
     * 初始化EduRecordSol
     */
    @PostConstruct
    public void init() {
        eduRecordSol = new EduRecordSol(address, client, client.getCryptoSuite().getCryptoKeyPair());
    }

    /**
     * 教育记录信息上链
     *
     * @param eduRecord 教育记录信息
     * @return 交易哈希
     */
    public String insert(EduRecordMainInfo eduRecord) {
        TransactionReceipt receipt = eduRecordSol.insert(
                eduRecord.getEduId(),
                eduRecord.getPersonId(),
                eduRecord.getEduType(),
                eduRecord.getNodeId(),
                eduRecord.getNodeName(),
                eduRecord.getBeginTime(),
                eduRecord.getEndTime(),
                eduRecord.getMajorName(),
                eduRecord.getAcquireCredit(),
                eduRecord.getGraduateCredit(),
                eduRecord.getCertifyFile());
        String insertRes = receipt.getStatus();
        if (Objects.equals(insertRes, ReceiptStatus.NORMAL)) {
            return receipt.getTransactionHash();
        }
        return null;
    }

    /**
     * 通过交易经历ID查询主要的教育经历信息
     *
     * @param eduId 教育经历ID
     * @return 主要的教育经历信息
     */
    public EduRecordMainInfo select(String eduId) {
        Tuple10<String, String, BigInteger, String, String, String, String, BigInteger, BigInteger, String> selectResponse;
        try {
            selectResponse = eduRecordSol.select(eduId);
            if (Objects.isNull(selectResponse)) {
                log.warn("未查询到上链的教育信息.");
                return null;
            }
            return new EduRecordMainInfo(
                    eduId,
                    selectResponse.getValue1(),
                    selectResponse.getValue2(),
                    selectResponse.getValue3(),
                    selectResponse.getValue4(),
                    selectResponse.getValue5(),
                    selectResponse.getValue6(),
                    selectResponse.getValue7(),
                    selectResponse.getValue8(),
                    selectResponse.getValue9(),
                    selectResponse.getValue10()
            );
        } catch (ContractException e) {
            log.warn("查询已上链教育经历异常.");
            e.printStackTrace();
        }
        return null;
    }
}
