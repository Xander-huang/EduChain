package top.zy68.personservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import top.zy68.personservice.api.ResponseBean;
import top.zy68.personservice.api.ResultCode;
import top.zy68.personservice.service.EduService;
import top.zy68.personservice.utils.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName EduRecordController
 * @Description 教育记录控制层
 * @Author Sustart
 * @Date2022/02/16 19:30
 * @Version 1.0
 **/
@Api(tags = "教育记录操作相关接口")
@RestController
@RequestMapping("eduRecord")
public class EduRecordController {

    @Resource
    EduService eduService;

    /**
     * 通过身份证号查询所有记录
     *
     * @param request HttpServletRequest
     * @return 多条记录
     */
    @ApiOperation("查询用户所有的教育记录（含课程成绩）")
    @GetMapping("/getAllEduRecord")
    public ResponseBean queryEduRecord(HttpServletRequest request) {
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        return new ResponseBean(ResultCode.SUCCESS, eduService.queryEduRecord(personId));
    }

    /**
     * 通过身份证号查询该用户参加过的所有机构
     *
     * @param request HttpServletRequest
     * @return 多条记录
     */
    @ApiOperation("查询该用户参加过的所有机构")
    @GetMapping("/getUserAlliance")
    public ResponseBean listUserAlliance(HttpServletRequest request) {
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        return new ResponseBean(ResultCode.SUCCESS, eduService.queryAllAlliance(personId));
    }

    /**
     * 个人用户选择性分享已上链数据
     *
     * @param eduIdList 要分享教育记录ID列表
     * @param request   HttpServletRequest
     * @return 多条记录
     */
    @ApiOperation("用户选择性分享教育信息")
    @PostMapping("/shareRecord")
    public ResponseBean shareDataInChain(@RequestParam("eduIdList") List<String> eduIdList, HttpServletRequest request) {
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        eduService.shareDataInChain(personId, eduIdList);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 获取区块链数据
     *
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @GetMapping("/getUplinkData")
    public ResponseBean getChainData(HttpServletRequest request) {
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        return new ResponseBean(ResultCode.SUCCESS, eduService.getChainData(personId));
    }


}
