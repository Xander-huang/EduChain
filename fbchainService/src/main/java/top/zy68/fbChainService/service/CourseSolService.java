package top.zy68.fbChainService.service;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.zy68.fbChainService.common.ReceiptStatus;
import top.zy68.fbChainService.model.chainMainInfo.CourseMainInfo;
import top.zy68.fbChainService.raw.CourseSol;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName CourseSolService
 * @Description 封装课程合约类的service，对外提供课程合约相关的服务。
 * @Author Sustart
 * @Date2022/04/04 21:44
 * @Version 1.0
 **/


@Service
@Slf4j
public class CourseSolService {
    @Value("${system.contract.courseSolAddress}")
    private String address;

    @Resource
    private Client client;

    CourseSol courseSol;

    /**
     * 初始化CourseSol
     */
    @PostConstruct
    public void init() {
        courseSol = new CourseSol(address, client, client.getCryptoSuite().getCryptoKeyPair());
    }

    /**
     * 课程记录上链
     *
     * @param course 教育经历的课程
     * @return 交易哈希
     */
    public String insert(CourseMainInfo course) {
        TransactionReceipt receipt = courseSol.insert(course.getEduId(), course.getNameList(), course.getCreditList(), course.getCertifyFileList());
        if (Objects.equals(receipt.getStatus(), ReceiptStatus.NORMAL)) {
            return receipt.getTransactionHash();
        }
        log.warn("课程信息上链失败");
        return null;
    }

    /**
     * 通过教育记录查询课程信息
     *
     * @param eduId 教育记录ID
     * @return 课程主要信息对象
     */
    public CourseMainInfo select(String eduId) {
        try {
            Tuple3<List<String>, List<String>, List<String>> selectResponse = courseSol.select(eduId);
            if (Objects.isNull(selectResponse)) {
                log.warn("未查询到上链的课程信息.");
                return null;
            }
            return new CourseMainInfo(eduId, selectResponse.getValue1(), selectResponse.getValue2(), selectResponse.getValue3());
        } catch (ContractException e) {
            log.warn("查询上链课程出现异常.");
            e.printStackTrace();
        }
        return null;
    }
}
