package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import top.zy68.fbChainService.entity.Person;


/**
 * @ClassName PersonDao
 * @Description 个人信息DAO
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface PersonDao {
    /**
     * 插入一条个人信息
     *
     * @param person 个人对象
     * @return 影响行数
     */
    int insertPerson(Person person);

    /**
     * 通过身份证号查询个人信息
     *
     * @param personId 身份正红
     * @return 个人对象
     */
    Person queryPersonByPersonId(String personId);
}

