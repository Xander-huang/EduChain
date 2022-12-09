package top.zy68.fbChainService.service;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.zy68.fbChainService.common.ReceiptStatus;
import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;
import top.zy68.fbChainService.raw.PersonSol;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName PersonSolService
 * @Description 封装个人信息合约类的service，对外提供个人信息合约相关的服务。
 * @Author Sustart
 * @Date2022/04/04 21:44
 * @Version 1.0
 **/

@Service
@Slf4j
public class PersonSolService {

    @Value("${system.contract.personSolAddress}")
    private String address;

    @Resource
    private Client client;

    PersonSol personSol;

    /**
     * 初始化PersonSol
     */
    @PostConstruct
    public void init() {
        personSol = new PersonSol(address, client, client.getCryptoSuite().getCryptoKeyPair());
    }

    /**
     * 查询已上链的个人信息
     *
     * @param personId 身份证号
     * @return 主要的个人信息
     */
    public PersonMainInfo select(String personId) {
        Tuple4<String, String, String, String> selectResponse;
        try {
            selectResponse = personSol.select(personId);
            if (Objects.isNull(selectResponse)) {
                log.warn("未查询到上链的个人信息.");
                return null;
            }
            return new PersonMainInfo(personId, selectResponse.getValue1(), selectResponse.getValue2(), selectResponse.getValue3(), selectResponse.getValue4());
        } catch (ContractException e) {
            log.warn("查询上链的个人信息异常.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上链一条个人信息记录
     *
     * @param input 个人信息记录
     * @return 交易哈希
     */
    public String insert(PersonMainInfo input) {
        TransactionReceipt receipt = personSol.insert(input.getPersonId(), input.getName(), input.getGender(), input.getNation(), input.getBirthday());
        String insertRes = receipt.getStatus();
        if (Objects.equals(insertRes, ReceiptStatus.NORMAL)) {
            return receipt.getTransactionHash();
        }
        log.warn("个人信息上链失败");
        return null;
    }
}
