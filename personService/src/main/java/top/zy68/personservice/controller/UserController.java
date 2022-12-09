package top.zy68.personservice.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.fbChainService.entity.PersonUser;
import top.zy68.personservice.api.ResponseBean;
import top.zy68.personservice.api.ResultCode;
import top.zy68.personservice.config.AesUtils;
import top.zy68.personservice.config.ConstantProperties;
import top.zy68.personservice.exception.BaseException;
import top.zy68.personservice.pojo.UserParam;
import top.zy68.personservice.pojo.uservo.ReqNewUserVo;
import top.zy68.personservice.pojo.uservo.ReqUserInfoVo;
import top.zy68.personservice.pojo.uservo.RspUserInfoVo;
import top.zy68.personservice.service.UserService;
import top.zy68.personservice.utils.CommonUtils;
import top.zy68.personservice.utils.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.zy68.fbChainService.utils.FileUtil.handPemFile;
import static top.zy68.personservice.enums.enums.CodeMessageEnums.*;

/**
 * @ClassName UserController
 * @Description 用户管理控制器
 * @Author Sustart
 * @Date2021/12/15 18:30
 * @Version 1.0
 **/
@RestController
@Slf4j
@Validated
@Api(tags = "用户信息操作相关接口")
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    ConstantProperties properties;

    @Resource
    AesUtils aesUtils;
    /**
     * 获取用户信息
     *
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @ApiOperation("查询用户信息")
    @GetMapping("/getUserInfo")
    public ResponseBean getUserInfo(HttpServletRequest request) {
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        return new ResponseBean(ResultCode.SUCCESS, userService.getUserInfo(personId));
    }

    /**
     * 获取用户教育相关信息
     *
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @ApiOperation("查询用户信息")
    @GetMapping("/getHomePageInfo")
    public ResponseBean getUserData(HttpServletRequest request) {
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        return new ResponseBean(ResultCode.SUCCESS, userService.getUserData(personId));
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
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        userService.updateUserPhone(personId, phone);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 更改用户信息-邮箱
     *
     * @param email   新邮箱
     * @param captcha 验证码
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @ApiOperation("更改邮箱地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true),
            @ApiImplicitParam(name = "captcha", value = "验证码", required = true)
    })
    @PostMapping(path = "/updateMail")
    public ResponseBean updateUserMail(@RequestParam("email") @NotEmpty @Email(message = "邮箱格式有误") String email,
                                       @RequestParam("captcha") @NotEmpty(message = "验证码不能为空") String captcha,
                                       HttpServletRequest request) {
        String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
        userService.updateUserMail(personId, email, captcha);
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
                                      @Length(min = 8, max = 12, message = "密码长度须为8~16个字符")
                                              String originPwd,
                                      @RequestParam("newPwd") @NotEmpty
                                      @Length(min = 8, max = 16, message = "密码长度须为8~16个字符")
                                              String newPwd,
                                      HttpServletRequest request,
                                      @RequestParam(value="f") MultipartFile file,
                                      HttpServletResponse response) {

        try {
            String personId = JwtUtil.getPersonId(request.getHeader("Authorization"));
            //String oldPri = readFileContent(file.getInputStream());

            if(Objects.isNull(file)){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "文件为空！");
            }
            if(!file.getOriginalFilename().endsWith(".pem")){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "文件格式不对！");
            }


            String pemFileJson = handPemFile(file);
            JSONObject jsonObject = JSONObject.parseObject(pemFileJson);
            if(Objects.isNull(jsonObject)){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "解析失败");
            }



            String s = userService.updateUserPwd(personId, originPwd, newPwd, jsonObject);
            byte[] result = s.getBytes();
            response.setContentType("application/vnd.ms-word;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=\"" + new String(("new key(use in login)"+"."+"pem").getBytes("gb2312"), "ISO8859-1"));
            ServletOutputStream os = response.getOutputStream();
            os.write(result);
            os.flush();
            os.close();
            System.out.println("下载成功");
        }catch (Exception e){
                e.printStackTrace();
            new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, null,e.getMessage());
        }

        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    private static String readFileContent(InputStream file) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = file.read()) != -1; ) {
            sb.append((char) ch);
        }
        String myString = sb.toString();
        return myString;
    }


/*    *//**
     * new user from ecdsa or guomi
     *//*
    @ApiOperation(value = "new user from ecdsa/guomi, default ecdsa",
            notes = "新建公私钥用户(ecdsa或国密)，默认ecdas")
    @GetMapping("/newUser")
    @ResponseBody
    public ResponseBean newUser(@RequestParam String signUserId, @RequestParam String appId,
                             @RequestParam(required = false, defaultValue = "0") Integer encryptType,
                             @RequestParam(required = false, defaultValue = "false") boolean returnPrivateKey)
            {
                

    
    }*/

    @ApiOperation(value = "import new user by private key", notes = "导入私钥用户(ecdsa或国密)，默认ecdas")
    @ApiImplicitParam(name = "reqNewUser", value = "private key info", required = true,
            dataType = "ReqNewUserVo")
    @PostMapping("/newUser")
    public ResponseBean newUserByImportPrivateKey(@RequestBody ReqNewUserVo reqNewUser,
                                               BindingResult result) throws BaseException {
        /*CommonUtils.checkParamBindResult(result);
        // validate signUserId
        String signUserId = reqNewUser.getSignUserId();
        String appId = reqNewUser.getAppId();
        Integer encryptType = reqNewUser.getEncryptType();
        String privateKeyEncoded = reqNewUser.getPrivateKey();
        if (StringUtils.isBlank(signUserId)) {
            throw new BaseException(PARAM_SIGN_USER_ID_IS_BLANK);
        }
        if (!CommonUtils.isLetterDigit(signUserId)
                || !CommonUtils.checkLengthWithin_64(signUserId)) {
            throw new BaseException(PARAM_SIGN_USER_ID_IS_INVALID);
        }
        if (StringUtils.isBlank(appId)) {
            throw new BaseException(PARAM_APP_ID_IS_BLANK);
        }
        if (!CommonUtils.isLetterDigit(appId) || !CommonUtils.checkLengthWithin_64(appId)) {
            throw new BaseException(PARAM_APP_ID_IS_INVALID);
        }
        if (encryptType != EncryptTypes.STANDARD.getValue()
                && encryptType != EncryptTypes.GUOMI.getValue()) {
            throw new BaseException(PARAM_ENCRYPT_TYPE_IS_INVALID);
        }
        // new user
        RspUserInfoVo userInfo =
                userService.newUser(signUserId, appId, encryptType, privateKeyEncoded);
        userInfo.setPrivateKey("");*/
        return new ResponseBean(ResultCode.SUCCESS,null, null);
    }

    /**
     * get user.
     */
    @ApiOperation(value = "check user info exist", notes = "check user info exist")
    @ApiImplicitParams({@ApiImplicitParam(name = "signUserId",
            value = "business id of user in system", required = true, dataType = "String"),})
    @GetMapping("/{signUserId}/userInfo")
    public ResponseBean getUserInfo(@PathVariable("signUserId") String signUserId,
                                 @RequestParam(required = false, defaultValue = "false") boolean returnPrivateKey)
            throws BaseException {
        if (!CommonUtils.checkLengthWithin_64(signUserId)) {
            throw new BaseException(PARAM_SIGN_USER_ID_IS_INVALID);
        }
        if (returnPrivateKey && !properties.isSupportPrivateKeyTransfer()) {
            throw new BaseException(PRIVATEKEY_NOT_SUPPORT_TRANSFER);
        }
        // find user
        PersonUser userInfo = userService.findBySignUserId(signUserId);
        RspUserInfoVo rspUserInfoVo = new RspUserInfoVo();
        Optional.ofNullable(userInfo).ifPresent(u -> BeanUtils.copyProperties(u, rspUserInfoVo));
        if (returnPrivateKey == false) {
            rspUserInfoVo.setPrivateKey("");
        } else {
            try{
                String s = aesUtils.aesEncrypt(userInfo.getPrivateKey());
                rspUserInfoVo.setPrivateKey(s);
            }catch (Exception e){
                log.error(e.getMessage());
                return new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR,rspUserInfoVo, e.getMessage());
            }

        }
        return new ResponseBean(ResultCode.SUCCESS,rspUserInfoVo, null);
    }

    /**
     * get user list by app id
     */
    @ApiOperation(value = "get user list by app id", notes = "根据appId获取user列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "appId", value = "app id that users belong to",
            required = true, dataType = "String"),})
    @GetMapping("/list/{appId}/{pageNumber}/{pageSize}")
    public ResponseBean getUserListByAppId(@PathVariable("appId") String appId,
                                        @PathVariable("pageNumber") Integer pageNumber,
                                        @PathVariable("pageSize") Integer pageSize,
                                        @RequestParam(required = false, defaultValue = "false") boolean returnPrivateKey)
            throws BaseException {
        if (!CommonUtils.checkLengthWithin_64(appId)) {
            throw new BaseException(PARAM_APP_ID_IS_INVALID);
        }
        if (returnPrivateKey == true && !properties.isSupportPrivateKeyTransfer()) {
            throw new BaseException(PRIVATEKEY_NOT_SUPPORT_TRANSFER);
        }
        UserParam param = new UserParam();
        param.setAppId(appId);
        int count = userService.countOfUser(param);
        List<RspUserInfoVo> userList = new ArrayList<>();
        if (count > 0) {
            Integer start =
                    Optional.ofNullable(pageNumber).map(page -> (page - 1) * pageSize).orElse(null);
            param.setStart(start);
            param.setPageSize(pageSize);
            // find user
            userList = userService.findUserListByAppId(param);
            if (!userList.isEmpty() && returnPrivateKey == false) {
                userList.forEach(user -> user.setPrivateKey(""));
            }
        }
        return new ResponseBean(ResultCode.SUCCESS,userList, null);
    }

    @ApiOperation(value = "delete user by address", notes = "通过地址删除私钥")
    @DeleteMapping("")
    public ResponseBean deleteUser(@RequestBody ReqUserInfoVo req) throws BaseException {
        String signUserId = req.getSignUserId();
        if (!CommonUtils.checkLengthWithin_64(signUserId)) {
            throw new BaseException(PARAM_SIGN_USER_ID_IS_INVALID);
        }
        // set as 0: SUSPENDED
        userService.deleteBySignUserId(signUserId);
        return new ResponseBean(ResultCode.SUCCESS,null, null);
    }


    @ApiOperation(value = "delete all user cache", notes = "删除所有用户缓存信息")
    @DeleteMapping("/all")
    public ResponseBean deleteAllUserCache() {

        userService.deleteAllUserCache();
        return new ResponseBean(ResultCode.SUCCESS,null, null);
    }

    @ApiOperation(value = "delete all Credential cache", notes = "删除所有私钥缓存信息")
    @DeleteMapping("/all-credential")
    public ResponseBean deleteCredentialCache() {

        userService.deleteAllCredentialCache();
        return new ResponseBean(ResultCode.SUCCESS,null, null);
    }
}
