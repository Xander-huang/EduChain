package top.zy68.allianceservice.controller;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.common.Constants;
import top.zy68.allianceservice.easyexcel.listener.MajorExcelListener;
import top.zy68.allianceservice.easyexcel.tableField.MajorExcel;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.service.MajorService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.allianceservice.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName MajorController
 * @Description 联盟专业类型控制层
 * @Author Sustart
 * @Date2022/03/15 19:30
 * @Version 1.0
 **/
@Slf4j
@RequiresRoles({"director"})
@RestController
@RequestMapping("major")
public class MajorController {
    @Resource
    private MajorService majorService;

    /**
     * 查询联盟点内的所有专业
     *
     * @param request HttpServletRequest
     * @return 新增结果
     */
    @GetMapping("/getAllMajors")
    public ResponseBean queryMajors(HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        return new ResponseBean(ResultCode.SUCCESS, majorService.queryAllMajors(IdUtil.getNodeId(account)));
    }

    /**
     * 通过教育类型查询专业
     *
     * @param majorType 教育类型
     * @param request   HttpServletRequest
     * @return 新增结果
     */
    @RequiresRoles(value = {"director", "teacher"}, logical = Logical.OR)
    @GetMapping("/getMajorByType/{majorType}")
    public ResponseBean getMajorByType(@PathVariable("majorType") String majorType, HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        return new ResponseBean(ResultCode.SUCCESS, majorService.getMajorsByType(IdUtil.getNodeId(account), majorType));
    }

    /**
     * 新增专业
     *
     * @param majorName 专业名称
     * @param majorType 专业类型
     * @param request   HttpServletRequest
     * @return 新增结果
     */
    @PostMapping("/addMajor")
    public ResponseBean addMajor(@RequestParam("name") String majorName,
                                 @RequestParam("majorType") String majorType,
                                 HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        return new ResponseBean(ResultCode.SUCCESS, majorService.addMajor(IdUtil.getNodeId(account), majorName, majorType));
    }

    /**
     * 批量新增专业
     *
     * @param file    新增专业的Excel
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @PostMapping("addMajors")
    @ResponseBody
    public ResponseBean majorExcelImport(MultipartFile file, HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        if (ObjectUtils.isEmpty(file)) {
            throw new ClientBusinessException("未接收到文件，请重试.");
        }
        log.info("MajorController.majorExcelImport received a file:" + file.getName());
        try {
            EasyExcel.read(file.getInputStream(), MajorExcel.class, new MajorExcelListener(majorService, IdUtil.getNodeId(account))).sheet().doRead();
        } catch (IOException e) {
            log.error("文件读取异常{}", e.getMessage());
            throw new ClientBusinessException("文件读取异常，请检查文件格式");
        }
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 删除专业
     *
     * @param majorId 专业编号
     * @return 新增结果
     */
    @PostMapping("/delMajor")
    public ResponseBean delMajor(@RequestParam("majorId") String majorId) {
        majorService.delMajor(majorId);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }
}

