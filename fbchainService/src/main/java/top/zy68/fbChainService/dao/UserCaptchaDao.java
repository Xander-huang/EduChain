package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.entity.UserCaptcha;


/**
 * @ClassName KeyDao
 * @Description KeyDao
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface UserCaptchaDao {

   UserCaptcha selectOneByCaptcha(String captcha);

   void insertOneCaptcha(@Param("userId")String user_id, @Param("captcha")String captcha,@Param("capsule")String capsule,@Param("ciphereText")byte[] ciphereText);

}

