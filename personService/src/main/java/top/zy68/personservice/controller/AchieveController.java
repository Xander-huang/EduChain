package top.zy68.personservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.personservice.api.ResponseBean;
import top.zy68.personservice.api.ResultCode;
import top.zy68.personservice.exception.ClientBusinessException;
import top.zy68.personservice.service.AchieveService;
import top.zy68.personservice.utils.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @ClassName AchieveController
 * @Description 成就记录管理控制器
 * @Author Sustart
 * @Date2022/02/16 19:30
 * @Version 1.0
 **/
@Api(tags = "成就记录操作相关接口")
@RestController
@Slf4j
@RequestMapping("achieveRecord")
public class AchieveController {

    @Resource
    AchieveService achieveService;
    @Value("${file.store.path}")
    private String filePath;

    /**
     * 增加一个成就记录
     *
     * @param eduId       教育经历ID
     * @param title       记录标题
     * @param type        记录类型
     * @param acquireTime 获得日期
     * @param remark      备注
     * @param files       文件
     * @return ResponseBean
     */
    @ApiOperation("增加一条成就记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eduId", value = "教育记录ID", required = true),
            @ApiImplicitParam(name = "title", value = "成就名称", required = true),
            @ApiImplicitParam(name = "type", value = "成就类型", required = true),
            @ApiImplicitParam(name = "acquireTime", value = "获得时间", required = true),
            @ApiImplicitParam(name = "remark", value = "备注"),
            @ApiImplicitParam(name = "files", value = "证书文件")
    })
    @PostMapping("/addAchieve")
    public ResponseBean addAchievementRecord(@RequestParam("eduId") @NotEmpty String eduId,
                                             @RequestParam("title") @NotEmpty String title,
                                             @RequestParam("type") @NotEmpty String type,
                                             @RequestParam("acquireTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date acquireTime,
                                             @RequestParam("remark") @Nullable String remark,
                                             @Nullable MultipartFile[] files
    ) {
        String filesId = null;
        if (ObjectUtils.isEmpty(files)) {
            log.info("提交成就未提交证明文件或提交失败.");
        } else {
            filesId = achieveService.uploadFile(files);
            if (!StringUtils.hasLength(filesId)) {
                throw new ClientBusinessException("文件上传失败");
            }
        }
        achieveService.insertAchieve(eduId, title, type, acquireTime, remark, filesId);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 获取用户的所有成就
     *
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @ApiOperation("查询用户所有成就")
    @GetMapping("/getAllAchieves")
    public ResponseBean queryAllAchieveRecord(HttpServletRequest request) {
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        return new ResponseBean(ResultCode.SUCCESS, achieveService.queryAllAchieveRecordByPersonId(personId));
    }

    /**
     * 获取用户已审核通过的所有成就
     *
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @ApiOperation("查询用户已通过的所有成就")
    @GetMapping("/getPassedAchieve")
    public ResponseBean queryPassedAchieveRecord(HttpServletRequest request) {
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        return new ResponseBean(ResultCode.SUCCESS, achieveService.queryPassedAchieve(personId));
    }
}

