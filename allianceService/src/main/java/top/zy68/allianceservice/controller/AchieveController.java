package top.zy68.allianceservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.common.Constants;
import top.zy68.allianceservice.pojo.vo.AchieveVO;
import top.zy68.allianceservice.service.AchieveRecordService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.allianceservice.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;


/**
 * @ClassName AchieveController
 * @Description 成就记录管理
 * @Author Sustart
 * @Date2022/3/15 13:31
 * @Version 1.0
 **/
@Slf4j
@RequiresRoles({"teacher"})
@RestController
@RequestMapping("achieveRecord")
public class AchieveController {

    @Resource
    AchieveRecordService achieveRecordService;

    /**
     * 获取所有学生的所有成就
     *
     * @param request HttpServletRequest
     * @return 查询结果
     */
    @GetMapping("/getAllStudentAchieves")
    public ResponseBean queryAllAchieves(HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        List<AchieveVO> achieves = achieveRecordService.queryAllAchieves(IdUtil.getNodeId(account));
        return new ResponseBean(ResultCode.SUCCESS, achieves);
    }

    /**
     * 修改认证成就状态
     *
     * @param id          成就id
     * @param auditStatus 成就状态
     * @param feedback    审核意见
     * @param request     HttpServletRequest
     * @return true/error
     */
    @PostMapping("/updateStudentAchieve")
    public ResponseBean updateAchievesStatus(@RequestParam("id") Integer id,
                                             @RequestParam("auditStatus") Short auditStatus,
                                             @RequestParam(value = "feedback", defaultValue = "") String feedback,
                                             HttpServletRequest request) {
        achieveRecordService.updateAchievesStatus(id, auditStatus, feedback);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 联盟用户添加学生成就
     *
     * @param eduId       学号
     * @param title       标题
     * @param type        类型
     * @param acquireTime 获得时间
     * @return ResponseBean
     */
    @PostMapping("/addStudentAchieve")
    @ResponseBody
    public ResponseBean insertStudentAchieve(@NotEmpty String eduId,
                                             @NotEmpty String title,
                                             @NotEmpty String type,
                                             @NotEmpty @DateTimeFormat(pattern = "yyyy-MM-dd") Date acquireTime) {
        achieveRecordService.insertAchieves(eduId, title, type, acquireTime);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }
}

