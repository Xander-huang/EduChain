package top.zy68.fbChainService.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.fbChainService.model.chainMainInfo.BlockMainInfo;

/**
 * @ClassName ClientServiceTest
 * @Description 区块链客户端服务方法测试
 * @Author Sustart
 * @Date2022/4/6 13:34
 * @Version 1.0
 **/
@SpringBootTest
class ClientServiceTest {

    @Autowired
    ClientService clientService;

    @Test
    void getBlockInfoByTransactionHash() {
        String transactionHash = "0xccec84ab5a534769558c9223cf82d20133f51efeb4af7dd2e971f58bed693d90";
        // String transactionHash = "0xf9a6c3d32a529716ae6ca88be1a9df391344ec0e9d7f9a40171d73e359bc98e2";
        BlockMainInfo blockMainInfo = clientService.getBlockInfoByTransactionHash(transactionHash);
        System.out.println(blockMainInfo.toString());
    }
}