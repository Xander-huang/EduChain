package top.zy68.allianceservice.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.common.Constants;
import top.zy68.allianceservice.service.UserService;
import top.zy68.allianceservice.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @ClassName UserController
 * @Description 用户自身信息管理
 * @Author Sustart
 * @Date2022/3/15 13:31
 * @Version 1.0
 **/
@Slf4j
@Validated
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 查询用户自己的信息
     *
     * @param request HttpServletRequest
     * @return 用户信息
     */
    @GetMapping("getUserInfo")
    public ResponseBean queryCurrUserInfo(HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        return new ResponseBean(ResultCode.SUCCESS, userService.queryUserInfo(account));
    }

    /**
     * 更改用户信息-手机号码
     *
     * @param phone   新手机号码
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @ApiOperation("更改联系电话")
    @ApiImplicitParam(name = "phone", value = "电话号码", required = true)
    @PostMapping(path = "/updatePhone")
    public ResponseBean updateUserPhone(@RequestParam("phone")
                                        @NotEmpty
                                        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话号码有误")
                                                String phone,
                                        HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        userService.updateUserPhone(account, phone);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 更改用户信息-密码
     *
     * @param originPwd 原密码
     * @param newPwd    新密码
     * @param request   HttpServletRequest
     * @return ResponseBean
     */
    @ApiOperation("更改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "originPwd", value = "原密码", required = true),
            @ApiImplicitParam(name = "newPwd", value = "新密码", required = true)
    })
    @PostMapping(path = "/updatePwd")
    public ResponseBean updateUserPwd(@RequestParam("originPwd") @NotEmpty
                                      @Length(min = 8, max = 16, message = "密码长度须为8~16个字符")
                                              String originPwd,
                                      @RequestParam("newPwd") @NotEmpty
                                      @Length(min = 8, max = 16, message = "密码长度须为8~16个字符")
                                              String newPwd,
                                      HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        userService.updateUserPwd(account, originPwd, newPwd);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }
}

