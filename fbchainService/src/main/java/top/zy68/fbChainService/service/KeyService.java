package top.zy68.fbChainService.service;


import org.apache.ibatis.annotations.Mapper;
import top.zy68.fbChainService.entity.Key;

/**
 * @ClassName ReproxyService
 * @Description ReproxyService
 * @Author Sustart
 * @Date2022/2/16 13:31
 * @Version 1.0
 **/
public interface KeyService {

    Key selectOneByIdCard(String personId);

    String generateOneKey(String idCard,Integer type) ;

}
