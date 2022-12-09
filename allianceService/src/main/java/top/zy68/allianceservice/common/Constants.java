package top.zy68.allianceservice.common;

import top.zy68.allianceservice.util.IdUtil;

import java.util.Objects;

/**
 * @ClassName Constants
 * @Description Alliance Constants 常量类
 * @Author Sustart
 * @Date2022/3/18 17:01
 * @Version 1.0
 **/
public class Constants {
    public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";

    /**
     * 判断联盟用户管理自身联盟是否非法操作：比较联盟点编号是否匹配
     * 非法：true，合法：false
     *
     * @param account 用户编号
     * @param ids     其他编号：专业id/课程id/学生id
     * @return 是否非法。
     */
    public static boolean isIllegal(String account, String ids) {
        return !Objects.equals(IdUtil.getNodeId(account), IdUtil.getNodeId(ids));
    }
}
