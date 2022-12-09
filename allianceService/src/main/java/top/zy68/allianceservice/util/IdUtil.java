package top.zy68.allianceservice.util;

import java.util.UUID;

/**
 * @ClassName IdUtil
 * @Description 用户账户工具类：获取联盟点编号
 * @Author Sustart
 * @Date2022/3/16 19:20
 * @Version 1.0
 **/
public class IdUtil {
    public static final int UID = 2;
    public static final int NODE_ID = 5;
    public static final int COURSE_ID = 3;
    public static final int MAJOR_ID = 3;

    /**
     * 从用户账号中截取联盟点编号
     *
     * @param account 用户账号
     * @return 联盟点编号
     */
    public static Integer getNodeId(String account) {
        return Integer.parseInt(account.substring(0, 5));
    }

    /**
     * 从学号中截取专业编号
     *
     * @param stuId 学号
     * @return 专业编号
     */
    public static String getMajorIdFromStuId(String stuId) {
        return stuId.substring(0, 8);
    }

    /**
     * 生成指定位数的随机数
     *
     * @param num 位数
     * @return 随机数字符串
     */
    public static String generateRandomNumber(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    /**
     * 生成 UUID
     *
     * @return 32个字符的字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 从学号中截取UUID
     *
     * @param stuId 学号：8位专业编号 + 32位UUID
     * @return 32位UUID
     */
    public static String getUUIDFromStuId(String stuId) {
        return stuId.substring(8);
    }

    /**
     * 从课号中截取专业编号
     *
     * @param courseId 课号
     * @return 专业编号
     */
    public static String getMajoridFromCourseId(String courseId) {
        return courseId.substring(0, 8);
    }
}
