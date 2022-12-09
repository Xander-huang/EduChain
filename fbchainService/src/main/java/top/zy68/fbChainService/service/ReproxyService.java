package top.zy68.fbChainService.service;


import top.zy68.fbChainService.entity.CheckKeyDto;
import top.zy68.fbChainService.entity.Reproxy;

import java.util.List;

/**
 * @ClassName ReproxyService
 * @Description ReproxyService
 * @Author Sustart
 * @Date2022/2/16 13:31
 * @Version 1.0
 **/
public interface ReproxyService {

    /**
     * 查询
     * @param captcha
     * @param userId
     * @return
     */
    Reproxy selectByCaptcha(String captcha, String userId);

    Reproxy selectById( Integer Id);

    /**
     * 插入
     * @param reproxy
     */
    Integer insertReproxy(Reproxy reproxy);


    /**
     * 更新
     * @param reproxy
     */
    void updateReproxy(Reproxy reproxy);


    void authorize(String captcha,CheckKeyDto keyDto);

    void sendEmailAuthorize(String email, Integer id,String senderName) throws Exception;

    void sendEmailAuthorize2(String email, Integer id,String senderName) throws Exception;
    String selectEmail(String id);


    /**
     * reproxy的id和a的私钥
     * @param id
     * @param priKey
     */
    void agree(Integer id,String priKey) throws Exception;

    /**
     * reproxy的id和a的私钥
     * @param id
     * @param priKey
     */
    void disagree(Integer id) throws Exception;



    List<Reproxy> list(String personId,Integer start,Integer pagNum) throws Exception;

}
