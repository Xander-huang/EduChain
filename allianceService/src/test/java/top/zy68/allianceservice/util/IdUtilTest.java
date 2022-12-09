package top.zy68.allianceservice.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @ClassName IdUtilTest
 * @Description TODO
 * @Author Sustart
 * @Date2022/3/16 19:25
 * @Version 1.0
 **/

public class IdUtilTest {

    @Test
    void getNodeNumber() {
        String account = "100010001";
        Assertions.assertEquals(10001, IdUtil.getNodeId(account));
    }

    /**
     * 测试随机编号生成
     */
    @Test
    void generateNumber() {
        System.out.println(IdUtil.generateRandomNumber(IdUtil.UID));
        System.out.println(IdUtil.generateRandomNumber(IdUtil.COURSE_ID));
        System.out.println(IdUtil.generateRandomNumber(IdUtil.MAJOR_ID));
        System.out.println(IdUtil.getUUID());
    }

    /**
     * 测试获取学号中的UUID
     */
    @Test
    void getUUIDFromStuId() {
        String stuId = "10000332657507a9c1744454a40aece19c5df092";
        Assertions.assertEquals("657507a9c1744454a40aece19c5df092", IdUtil.getUUIDFromStuId(stuId));
    }

    @Test
    void getMajorIdFromStuId() {
        String stuId = "10000332657507a9c1744454a40aece19c5df092";
        Assertions.assertEquals("10000332", IdUtil.getMajorIdFromStuId(stuId));
    }
}