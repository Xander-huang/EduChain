package top.zy68.personservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.fbChainService.config.Mail;
import top.zy68.fbChainService.service.KeyService;
import top.zy68.personservice.pojo.UserInfoPo;
import top.zy68.personservice.pojo.uservo.RspUserInfoVo;
import top.zy68.personservice.service.IKeyStoreService;
import top.zy68.personservice.service.UserService;

import javax.annotation.Resource;

@SpringBootTest
class PersonServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    Mail mailDO;

    @Resource
    KeyService keyService;

     @Test
    public void defaultValueTest() {
         String reProxyPri = keyService.generateOneKey("340822199812010101", 0);
    }

    @Resource
    IKeyStoreService keyStoreService;

    @Resource
    UserService userService;
    //
    @Test
    public void name() {
        String signUserId = "123456";
        String appId = null;
        Integer encryptType = 0;
        boolean returnPrivateKey = true;
        try {
            // new user
            UserInfoPo userInfo = userService.newUser(signUserId, encryptType, null,"");
            if (returnPrivateKey == false) {
                userInfo.setPrivateKey("");
            }
            System.out.println(userInfo);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println();
    }
}
