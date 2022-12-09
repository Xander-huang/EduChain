package top.zy68.allianceservice.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.service.LoginService;
import top.zy68.allianceservice.service.UserService;
import top.zy68.fbChainService.entity.NodeUser;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @ClassName LoginController
 * @Description 用户登录管理
 * @Author Sustart
 * @Date2022/3/13 20:31
 * @Version 1.0
 **/
@Validated
@RestController
@Slf4j
public class LoginController {

    @Resource
    LoginService loginService;
    @Resource
    UserService userService;

    /**
     * 登录
     *
     * @param username 用户名（账号/密码）
     * @param password 密码
     * @return ResponseBean 含token
     */
    @PostMapping("/login")
    public ResponseBean login(@RequestParam("account") String username,
                              @RequestParam("password") String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            throw new ClientBusinessException("不存在该用户.");
        } catch (IncorrectCredentialsException e) {
            throw new ClientBusinessException("用户名或密码错误.");
        }

        // Shiro认证通过后会将user信息放到subject内，生成token并返回
        NodeUser currUser = (NodeUser) subject.getPrincipal();
        String newToken = userService.generateAccountToken(currUser.getAccount());

        log.info("User: {} login.", currUser.getAccount());
        return new ResponseBean(ResultCode.SUCCESS, newToken);
    }

    /**
     * 登出
     *
     * @return ResponseBean
     */
    @PostMapping("/logout")
    public ResponseBean logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            NodeUser currUser = (NodeUser) subject.getPrincipals().getPrimaryPrincipal();
            // 删除当前用户的登录信息（即token的Salt）
            userService.deleteLoginInfo(currUser.getAccount());
            log.info("User: {} logout.", currUser.getAccount());
        }
        SecurityUtils.getSubject().logout();
        return new ResponseBean(ResultCode.SUCCESS, null);
    }


    /**
     * 登录的验证码（含验证码和图片编码）
     *
     * @return 结果视图对象
     * @Description 生成并返回经过Base64编码的验证码图片。前端将其（data）放入img标签的src中
     */
    @ApiOperation("获取图片验证码")
    @GetMapping("/getCaptchaImg")
    public ResponseBean loginCaptcha() {
        return new ResponseBean(ResultCode.SUCCESS, loginService.getLoginCaptcha());
    }

    /**
     * 给指定邮箱发送验证邮箱的邮件
     *
     * @param email 邮箱
     * @return ResponseBean
     */
    @ApiOperation("发送邮件")
    @ApiImplicitParam(name = "email", value = "邮箱地址", required = true)
    @PostMapping("/sendEmailCaptcha/{email}")
    public ResponseBean sendEmailCaptcha(@PathVariable("email") @NotEmpty @Email(message = "邮箱格式有误") String email) {
        loginService.sendEmailVerifyCode(email);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 用户忘记密码
     *
     * @param email   邮箱地址
     * @param newPwd  新密码
     * @param captcha 邮件验证码
     * @return ResponseBean
     */
    @ApiOperation("重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱地址", required = true),
            @ApiImplicitParam(name = "newPwd", value = "新密码", required = true),
            @ApiImplicitParam(name = "captcha", value = "验证码", required = true)
    })
    @PostMapping("/resetPwd")
    public ResponseBean resetUserPwd(@RequestParam("email")
                                     @NotEmpty(message = "邮箱不能为空")
                                     @Email(message = "邮箱格式错误")
                                             String email,
                                     @NotEmpty(message = "密码不能为空")
                                     @RequestParam("newPwd")
                                     @Length(min = 8, max = 16, message = "密码长度须为8~16个字符")
                                             String newPwd,
                                     @RequestParam("captcha")
                                     @NotEmpty(message = "验证码不能为空")
                                             String captcha) {
        loginService.resetUserPwd(email, newPwd, captcha);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }
}
