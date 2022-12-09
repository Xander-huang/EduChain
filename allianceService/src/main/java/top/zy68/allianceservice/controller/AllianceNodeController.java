package top.zy68.allianceservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.common.Constants;
import top.zy68.allianceservice.pojo.dto.NodeRegisterDTO;
import top.zy68.allianceservice.service.AllianceNodeService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.allianceservice.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ClassName AllianceNodeController
 * @Description 联盟点信息控制层
 * @Author Sustart
 * @Date2022/4/11 8:49
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("alliance")
public class AllianceNodeController {

    @Resource
    AllianceNodeService allianceNodeService;

    /**
     * 注册一个机构
     *
     * @param registerDTO 注册传输对象
     * @return ResponseBean
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseBean register(@RequestBody @Valid NodeRegisterDTO registerDTO) {
        return new ResponseBean(ResultCode.SUCCESS, allianceNodeService.registerNodeNotification(registerDTO));
    }

    /**
     * 机构注册审批
     *
     * @param key      申请注册的 redis key
     * @param auditRes 审批结果
     * @return ResponseBean
     */
    @GetMapping(value = "/doAudit/{key}/{auditRes}")
    public ResponseBean registerAudit(@PathVariable("key") String key, @PathVariable("auditRes") String auditRes) {
        return new ResponseBean(ResultCode.SUCCESS, allianceNodeService.auditRegister(key, auditRes));
    }


    /**
     * 获取节点相关数据
     *
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @GetMapping("getHomePageInfo")
    public ResponseBean getNodeData(HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        return new ResponseBean(ResultCode.SUCCESS, allianceNodeService.getNodeData(IdUtil.getNodeId(account)));
    }
}
