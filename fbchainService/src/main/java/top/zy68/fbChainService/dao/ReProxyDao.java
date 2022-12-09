package top.zy68.fbChainService.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zy68.fbChainService.entity.Reproxy;

import java.util.List;

/**
 * @ClassName ReProxyDao
 * @Description ReProxyDao
 * @Author Sustart
 * @Date2022/3/21 17:01
 * @Version 1.0
 **/
@Mapper
public interface ReProxyDao {
   Reproxy selectByCaptcha(@Param("captcha") String captcha, @Param("receiverId") String receiverId);

   Reproxy selectById(@Param("id")Integer id);

   Integer insertReproxy(Reproxy reproxy);

   void updateReproxy(Reproxy reproxy);

   String selectEmail(String id);


   List<Reproxy> selectByPersonId(@Param("personId") String personId,@Param("start")Integer start,
                                  @Param("pageNum")Integer pageNum);

}

