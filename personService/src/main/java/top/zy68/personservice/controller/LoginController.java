package top.zy68.personservice.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.fbChainService.entity.PersonUser;
import top.zy68.personservice.api.ResponseBean;
import top.zy68.personservice.api.ResultCode;
import top.zy68.personservice.config.AesUtils;
import top.zy68.personservice.dto.RegisterDTO;
import top.zy68.personservice.service.LoginService;
import top.zy68.personservice.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

import static top.zy68.fbChainService.utils.FileUtil.handPemFile;

/**
 * @ClassName LoginController
 * @Description 登录控制器
 * @Author Sustart
 * @Date2022/1/16 19:07
 * @Version 1.0
 **/
@Api(tags = "登录相关接口")
@Validated
@Slf4j
@RestController
public class LoginController {
    @Resource
    UserService userService;
    @Resource
    LoginService loginService;

    @Resource
    AesUtils aesUtils;

    /**
     * 注册
     *
     * @param registerDTO 注册传输对象
     * @return ResponseBean
     */
    @ApiOperation("注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseBean register(@RequestBody @Valid RegisterDTO registerDTO, HttpServletResponse response) {
        log.info("received a register request: {}", registerDTO.getPersonId());

        try {
           loginService.register(registerDTO,response);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, null,e.getMessage());
        }
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 给指定邮箱发送验证邮箱的邮件
     *
     * @param email 邮箱
     * @return ResponseBean
     */
    @ApiOperation("发送邮件")
    @ApiImplicitParam(name = "mail", value = "邮箱地址", required = true)
    @PostMapping("/sendEmailCaptcha/{email}")
    public ResponseBean sendEmailCaptcha(@PathVariable("email") @NotEmpty @Email(message = "邮箱格式有误") String email) {
        loginService.sendEmailVerifyCode(email);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 登录
     * 支持 “身份证号/邮箱 + 密码” 的两种登录方式
     *
     * @param account  账户：身份证号/邮箱
     * @param password 密码
     * @return ResponseBean 返回登录结果，成功则包含token；否则，根据具体情况反馈失败信息.
     */
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账户", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    @PostMapping("/login")
    public ResponseBean login(@RequestParam("account") String account,
                              @RequestParam("password") @NotEmpty(message = "密码不能为空")
                              @Length(min = 8, max = 12, message = "密码长度须为8~12个字符")
                                      String password,
                              @RequestParam("file") MultipartFile file) {
        if(Objects.isNull(file)){
            return new ResponseBean(ResultCode.BAD_REQUEST,null, "文件为空！");
        }
        if(!file.getOriginalFilename().endsWith(".pem")){
            return new ResponseBean(ResultCode.BAD_REQUEST,null, "文件格式不对！");
        }

        Subject subject = SecurityUtils.getSubject();
        // 封装用户请求参数，直接提交给Shiro处理，Shiro会调用AccountAuthRealm处理。
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(account, password);
        subject.login(usernamePasswordToken);
        // Shiro认证通过后会将user信息放到subject内，生成token并返回
        PersonUser currUser = (PersonUser) subject.getPrincipal();

        try {

            //校验pem私钥文件
            String pemFileJson = handPemFile(file);
            password = AesUtils.handPwdLength(password);
            String json = aesUtils.aesDecrypt(pemFileJson, password, "");
            JSONObject jsonObject = null;
            try {
                jsonObject = JSONObject.parseObject(json);
            }catch (Exception e){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "私钥不正确！");
            }

            if(Objects.isNull(jsonObject)){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "私钥不正确！");
            }
            String address = jsonObject.getString("address");
            if(Objects.isNull(address) || !Objects.equals(address,currUser.getAddress())){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "私钥不正确！");
            }

            // 生成Token
            String newToken = userService.generateAccountToken(currUser.getPersonId());
            log.info("User: {} login.", account);
            return new ResponseBean(ResultCode.SUCCESS, newToken);
        }catch (Exception e){
            return new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, null);
        }

    }

    /**
     * 登出
     *
     * @return ResponseBean
     */
    @ApiOperation("登出")
    @PostMapping("/logout")
    public ResponseBean logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            PersonUser currUser = (PersonUser) subject.getPrincipals().getPrimaryPrincipal();
            // 删除当前用户的登录信息（即token的Salt）
            userService.deleteLoginInfo(currUser.getPersonId());
            log.info("User: {} logout.", currUser.getPersonId());
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
     * 忘记密码
     *
     * @param email    邮箱地址
     * @param password 新密码
     * @param captcha  邮件验证码
     * @return ResponseBean
     */
    @ApiOperation("重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mail", value = "邮箱地址", required = true),
            @ApiImplicitParam(name = "newPwd", value = "新密码", required = true),
            @ApiImplicitParam(name = "captcha", value = "验证码", required = true)
    })
    @PostMapping("/resetPwd")
    public ResponseBean resetUserPwd(@RequestParam("email")
                                     @NotEmpty(message = "邮箱不能为空")
                                     @Email(message = "邮箱格式错误")
                                             String email,
                                     @NotEmpty(message = "密码不能为空")
                                     @RequestParam("password")
                                     @Length(min = 8, max = 12, message = "密码长度须为8~12个字符")
                                             String password,
                                     @RequestParam("captcha")
                                     @NotEmpty(message = "验证码不能为空")
                                             String captcha) {
        userService.resetUserPwd(email, password, captcha);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

}


