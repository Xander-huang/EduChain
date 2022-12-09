package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import top.zy68.fbChainService.entity.PersonUser;

import java.util.List;

/**
 * @ClassName PersonUserDao
 * @Description 个人端用户DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface PersonUserDao {
    /**
     * 插入一个个人用户
     *
     * @param personUser 个人用户对象
     * @return 影响行数
     */
    int insertPersonUser(PersonUser personUser);

    /**
     * 通过身份证号查询个人用户
     *
     * @param personId 身份证号
     * @return 个人用户对象
     */
    PersonUser queryPersonUserByPersonId(String personId);

    /**
     * 更新个人用户
     *
     * @param personUser 个人用户对象
     * @return 影响行数
     */
    int updatePersonUserByPersonId(PersonUser personUser);

    /**
     * 通过邮箱账号查询个人用户
     *
     * @param email 邮箱账号
     * @return 个人用户对象
     */
    PersonUser queryPersonUserByEmail(String email);


    PersonUser  findUserBySignUserId(String signUserId);

}

