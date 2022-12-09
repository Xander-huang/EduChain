package top.zy68.fbChainService.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;

import java.util.Objects;

/**
 * @ClassName PersonSolServiceTest
 * @Description 测试PersonSolService服务方法
 * @Author Sustart
 * @Date2022/4/4 11:21
 * @Version 1.0
 **/
@SpringBootTest
class PersonSolServiceTest {

    @Autowired
    PersonSolService personSolService;

    @Test
    void select() {
        String personId = "458098822278654329";
        try {
            PersonMainInfo mainInfo = personSolService.select(personId);
            System.out.println(mainInfo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void insert() {
        PersonMainInfo personMainInfo = new PersonMainInfo("458098822278654333", "张三", "男", "汉族", "2000-03-02");
        //resHash = 0xbe1a74a2bd7a0f42aa161905450e35c864e07252b43b6a00bd81af638bf1321a
        try {
            String transactionHash = personSolService.insert(personMainInfo);
            if (Objects.isNull(transactionHash)) {
                System.out.println("上链异常");
            }
            System.out.println(transactionHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}