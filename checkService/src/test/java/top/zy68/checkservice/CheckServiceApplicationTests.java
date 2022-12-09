package top.zy68.checkservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.fbChainService.service.KeyService;
import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;
import top.zy68.fbChainService.service.PersonSolService;
import top.zy68.fbChainService.service.ReproxyService;

import javax.annotation.Resource;

@SpringBootTest
class CheckServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    PersonSolService personSolService;

    @Resource
    ReproxyService reproxyService;


    @Resource
    KeyService keyService;
    @Test
    public void TestRegister() {


        keyService.generateOneKey("610115199901100514",1);
//        this.insertOneKey();

    }

}
