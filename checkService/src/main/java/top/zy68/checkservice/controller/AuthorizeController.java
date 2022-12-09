package top.zy68.checkservice.controller;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import top.zy68.checkservice.common.ResponseBean;
import top.zy68.checkservice.common.ResultCode;
import top.zy68.fbChainService.entity.Reproxy;
import top.zy68.fbChainService.service.ReproxyService;



import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.zy68.fbChainService.utils.FileUtil.handPemFile;


@RestController
@RequestMapping("/authorize")
public class AuthorizeController {

    @Resource
    ReproxyService reproxyService;


    /**
     * 同意授权
     *
     * @param
     * @return ResponseBean
     */
    @PostMapping("/{id}/agree")
    public ResponseBean agree(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file)  {

        try {
            if(Objects.isNull(file)){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "文件为空！");
            }
            if(!file.getOriginalFilename().endsWith(".pem")){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "文件格式不对！");
            }

            String pemFileJson = handPemFile(file);

            if(StringUtils.isEmpty(pemFileJson)){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "私钥不对！");

            }
            reproxyService.agree(id,pemFileJson);
        }catch (Exception e){
            return new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return new ResponseBean(ResultCode.SUCCESS,"授权成功");

    }

    /**
     * 拒绝授权
     *
     * @param
     * @return ResponseBean
     */
    @PostMapping("/{id}/disagree")
    public ResponseBean disagree(@PathVariable("id") Integer id)  {

        try {
            reproxyService.disagree(id);
        }catch (Exception e){
            return new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return new ResponseBean(ResultCode.SUCCESS,"拒绝成功");

    }

    /**
     * 查询列表
     *
     * @param
     * @return ResponseBean
     */
    @GetMapping("/list/{id}")
    public ResponseBean list(@PathVariable("id") String id,
                             @RequestParam(defaultValue = "0")Integer start,
                             @RequestParam(defaultValue = "10")Integer pageNum
    )  {
        List<Reproxy> list ;
        try {
            list = reproxyService.list(id, start, pageNum);
        }catch (Exception e){
            return new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return new ResponseBean(ResultCode.SUCCESS,list,"查询成功");

    }



}
