package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.entity.Key;


/**
 * @ClassName KeyDao
 * @Description KeyDao
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface KeyDao {

   Key selectOneByIdCard(String personId);


    /**
     * 插入一条密钥
     * @param userId
     * @param publicKey
     * @param type
     */
    void insertAnKey(@Param("userId") String userId, @Param("publicKey") String publicKey, @Param("type") Integer type);

    /**
     * 通过用户id找公钥
     * @param userId
     * @return
     */
    String selectPubKeyById(String userId);

}

