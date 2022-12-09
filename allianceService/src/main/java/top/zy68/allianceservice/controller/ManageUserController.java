package top.zy68.allianceservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.common.Constants;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.service.ManageUserService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.allianceservice.util.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * @ClassName ManageUserController
 * @Description （顶级管理员）用户管理
 * @Author Sustart
 * @Date2022/3/15 13:31
 * @Version 1.0
 **/
@Slf4j
@Validated
@RequiresRoles({"admin"})
@RestController
@RequestMapping("manage")
public class ManageUserController {

    @Resource
    ManageUserService manageUserService;

    /**
     * 查询所有的用户
     *
     * @param request HttpServletRequest
     * @return 用户信息
     */
    @GetMapping("getUsers")
    public ResponseBean queryAllUsers(HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        return new ResponseBean(ResultCode.SUCCESS, manageUserService.queryAllUsers(IdUtil.getNodeId(account)));
    }

    /**
     * 添加用户
     *
     * @param name    用户名称
     * @param email   邮箱
     * @param phone   手机号码
     * @param role    角色
     * @param request HttpServletRequest获取请求者
     * @return ResponseBean
     */
    @PostMapping("addUser")
    public ResponseBean addUser(@RequestParam("name") @NotEmpty String name,
                                @RequestParam("email") @NotEmpty @Email(message = "邮箱格式有误") String email,
                                @RequestParam("phone") @NotEmpty @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话号码有误")
                                        String phone,
                                @RequestParam("role") @NotEmpty String role,
                                HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        return new ResponseBean(ResultCode.SUCCESS, this.manageUserService.insertUser(account, name, email, phone, role));
    }

    /**
     * 修改用户信息
     *
     * @param account 账号
     * @param name    姓名
     * @param email   邮箱
     * @param phone   手机号码
     * @param role    角色
     * @param request HttpServletRequest
     * @return 修改结果
     */
    @PostMapping("updateUser")
    public ResponseBean updateUser(@RequestParam("account") String account,
                                   @RequestParam("name") String name,
                                   @RequestParam("email") @Email(message = "邮箱格式有误") String email,
                                   @RequestParam("phone") @NotEmpty @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话号码有误")
                                           String phone,
                                   @RequestParam("role") String role,
                                   HttpServletRequest request) {
        String operator = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        if (isLegal(operator, account)) {
            throw new ClientBusinessException("操作非法，不可跨联盟点删除用户");
        }
        this.manageUserService.updateUserMainInfo(account, name, email, phone, role);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 删除用户
     *
     * @param account 账户
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @PostMapping("delUser")
    public ResponseBean deleteUser(
            @RequestParam("account") @NotEmpty String account,
            HttpServletRequest request) {
        String operatorAccount = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        if (isLegal(operatorAccount, account)) {
            throw new ClientBusinessException("操作非法，不可跨联盟点删除用户");
        }
        manageUserService.deleteByAccount(account);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 重置用户密码为默认（手机号码后八位）
     *
     * @param account 账户
     * @param request HttpServletRequest
     * @return 重置结果
     */
    @PostMapping("resetUserPwd")
    public ResponseBean resetUserPwdToDefault(@RequestParam("account") @NotEmpty String account, HttpServletRequest request) {
        String operator = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        if (isLegal(operator, account)) {
            throw new ClientBusinessException("操作非法，不可跨联盟点删除用户");
        }
        this.manageUserService.updateUserPwdToDefault(operator, account);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 根据token内的account来判断该管理员即将操作的用户是否是其联盟的用户.
     *
     * @param operator 顶级管理员账户
     * @param account  将被管理的用户账户
     * @return 是否是同一个联盟的用户
     */
    private boolean isLegal(String operator, String account) {
        return !Objects.equals(IdUtil.getNodeId(operator), IdUtil.getNodeId(account));
    }
}

