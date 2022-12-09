package top.zy68.fbChainService.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.fbChainService.model.chainMainInfo.EduRecordMainInfo;

import javax.annotation.Resource;

import java.math.BigInteger;

/**
 * @ClassName EduRecordSolServiceTest
 * @Description 教育经历上链服务方法测试
 * @Author Sustart
 * @Date2022/4/6 21:21
 * @Version 1.0
 **/
@SpringBootTest
class EduRecordSolServiceTest {

    @Resource
    EduRecordSolService eduRecordSolService;

    // @Test
    void insert() {
        EduRecordMainInfo mainInfo = new EduRecordMainInfo(
                "10000456yuuiij893n3b",
                "4506654322289765221",
                "本科",
                new BigInteger("10000"),
                "北京大学",
                "2020-09-09",
                "2024-06-30",
                "计算机科学与技术",
                new BigInteger("250"),
                new BigInteger("300"),
                "file.pdf"
        );
        String transactionHash = eduRecordSolService.insert(mainInfo);
        System.out.println(transactionHash);
    }

    @Test
    void select() {
        EduRecordMainInfo mainInfo = eduRecordSolService.select("10000456yuuiij893n3b");
        System.out.println(mainInfo.toString());
    }
}