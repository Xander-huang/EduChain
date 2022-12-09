package top.zy68.allianceservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.allianceservice.pojo.vo.UserVO;

import javax.annotation.Resource;

/**
 * @ClassName ManageUserServiceTest
 * @Description
 * @Author Sustart
 * @Date2022/4/8 22:40
 * @Version 1.0
 **/
@SpringBootTest
class ManageUserServiceTest {

    @Resource
    ManageUserService manageUserService;

    /**
     * 创建用户（管理员用户产生方法）
     */
    @Test
    void insertUser() {
        String operator = "1000000";
        String name = "李四老师";
        String email = "2573022290@qq.com";
        String phone = "18685464282";
        String role = "director";
        UserVO userVO = manageUserService.insertUser(operator, name, email, phone, role);
        System.out.println(userVO.toString());
    }
}