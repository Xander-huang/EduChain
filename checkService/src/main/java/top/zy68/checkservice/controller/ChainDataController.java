package top.zy68.checkservice.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.checkservice.common.ResponseBean;
import top.zy68.checkservice.common.ResultCode;
import top.zy68.fbChainService.dao.PersonUserDao;
import top.zy68.fbChainService.entity.*;
import top.zy68.fbChainService.grpc.recrypt.ReCrypt;
import top.zy68.fbChainService.grpc.recrypt.ReCryptGrpc;
import top.zy68.checkservice.model.pojo.ChainAchieve;
import top.zy68.checkservice.model.pojo.ChainCourse;
import top.zy68.checkservice.model.pojo.ShareDataBO;
import top.zy68.checkservice.model.vo.BasicVO;
import top.zy68.checkservice.model.vo.DataVO;
import top.zy68.checkservice.model.vo.EducationVO;
import top.zy68.fbChainService.service.KeyService;
import top.zy68.fbChainService.service.ReproxyService;
import top.zy68.fbChainService.service.UserCaptchaService;
import top.zy68.fbChainService.model.chainMainInfo.AchieveMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.BlockMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.CourseMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;
import top.zy68.fbChainService.service.*;

import javax.annotation.Resource;
import java.util.*;

import static top.zy68.fbChainService.common.ApiConst.GrpcConst.host;
import static top.zy68.fbChainService.common.ApiConst.GrpcConst.serverPort;
import static top.zy68.fbChainService.utils.FileUtil.handPemFile;

/**
 * @ClassName ChainDataController
 * @Description 区块链访问控制层
 * @Author Sustart
 * @Date2022/4/9 19:10
 * @Version 1.0
 **/
@RestController
public class ChainDataController {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    PersonSolService personSolService;
    @Resource
    EduRecordSolService eduRecordSolService;
    @Resource
    CourseSolService courseSolService;
    @Resource
    AchieveSolService achieveSolService;
    @Resource
    ClientService clientService;

    @Resource
    KeyService keyService;

    @Resource
    ReproxyService reproxyService;

    @Autowired
    @Qualifier("bytesRedisTemplate")
    RedisTemplate<String, byte[]> bytesRedisTemplate;

    @Resource
    UserCaptchaService userCaptchaService;

    @Resource
    PersonUserDao personUserDao;




    /**
     * 获取区块链数据
     *
     * @param captcha 验证码
     * @return ResponseBean
     */
    @PostMapping("/authorize/{captcha}")
    public ResponseBean authorize(@PathVariable("captcha") String captcha,
                                     @RequestBody CheckKeyDto keyDto) {


        if(StringUtils.isEmpty(keyDto.getPersonId())){
            return new ResponseBean(ResultCode.BAD_REQUEST, "身份证为空！");
        }

        //校验验证码
        byte[] bytes = bytesRedisTemplate.opsForValue().get(captcha);
        if (bytes == null) {
            return new ResponseBean(ResultCode.BAD_REQUEST, "不存在该校验码！");
        }
        //校验身份信息
        PersonUser personUser = personUserDao.queryPersonUserByPersonId(keyDto.getPersonId());

        if(Objects.isNull(personUser)){
            return new ResponseBean(ResultCode.BAD_REQUEST, "不存在该身份的用户,请先注册！");
        }

        //查询该验证码的状态
        Reproxy reproxy = reproxyService.selectByCaptcha(captcha,keyDto.getPersonId());
        if(Objects.nonNull(reproxy) && reproxy.getState() >0 ){
            return new ResponseBean(ResultCode.SUCCESS, "已经提交授权！无需重复提交！");
        }

        UserCaptcha userCaptcha = userCaptchaService.selectOneByCaptcha(captcha);
        if (Objects.isNull(userCaptcha)){
            return new ResponseBean(ResultCode.SUCCESS, "未找到验证码对应的用户！");
        }

        //插入一条记录
        Reproxy reproxy1 = new Reproxy();
        reproxy1.setState(1);
        reproxy1.setReceiverId(keyDto.getPersonId());
        reproxy1.setCaptcha(captcha);
        reproxy1.setSenderId(userCaptcha.getUserId());
        reproxy1.setCapsule(userCaptcha.getCapsule());
        reproxy1.setCipherText(userCaptcha.getCiphereText());
        reproxy1.setCreateTime(new Date());
        reproxy1.setUpdateTime(new Date());

        reproxyService.insertReproxy(reproxy1);

        if(Objects.isNull(reproxy1.getId())){
            new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, "数据库错误！");
        }
        String email = reproxyService.selectEmail(userCaptcha.getUserId());
        try {
            //建立链接
            reproxyService.sendEmailAuthorize(email,reproxy1.getId(),personUser.getName());
        } catch (Exception e) {
            new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return  new ResponseBean(ResultCode.SUCCESS, "提交申请成功，请等待同意！");

    }

    /**
     * 获取区块链数据
     *
     * @param captcha 验证码
     * @return ResponseBean
     */
    @PostMapping("/check/{captcha}")
    public ResponseBean getChainData(@PathVariable("captcha") String captcha,
                                     @RequestParam("personId") String personId,
                                     @RequestParam("file") MultipartFile file) {
        try {
            if(Objects.isNull(file)){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "文件为空！");
            }
            if(!file.getOriginalFilename().endsWith(".pem")){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "文件格式不对！");
            }

            String pemFileJson = handPemFile(file);

            if(org.apache.commons.lang3.StringUtils.isEmpty(pemFileJson)){
                return new ResponseBean(ResultCode.BAD_REQUEST,null, "私钥不对！");

            }

            //todo 改成文件上传的接口
            if(StringUtils.isEmpty(personId)){
                return new ResponseBean(ResultCode.BAD_REQUEST, "身份证为空！");
            }

            //校验验证码
            String shareDataJson = stringRedisTemplate.opsForValue().get(captcha);
            if (shareDataJson == null) {
                return new ResponseBean(ResultCode.BAD_REQUEST, "不存在该校验码");
            }

            Key key = keyService.selectOneByIdCard(personId);

            if(Objects.isNull(key)){
                return new ResponseBean(ResultCode.BAD_REQUEST, "不存在该身份的用户");
            }

            //查询该验证码的状态
            Reproxy reproxy = reproxyService.selectByCaptcha(captcha,personId);
            if(Objects.isNull(reproxy)){
                return new ResponseBean(ResultCode.BAD_REQUEST, "不存在该校验码");
            }
            Integer state = reproxy.getState();

            if(Objects.equals(1,state)){
                return new ResponseBean(ResultCode.SUCCESS, "正在授权，请等待处理。");
            }else if(Objects.equals(2,state)){
                return new ResponseBean(ResultCode.SUCCESS, "授权失败，用户拒绝授权！");
            }


           // String privateKey = keyDto.getPrivKey();
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, serverPort)
                    .usePlaintext().build();
            ReCryptGrpc.reCryptBlockingStub reCryptBlockingStub = ReCryptGrpc.newBlockingStub(channel);

            ReCrypt.DeCryptRequest deCryptRequest = ReCrypt.DeCryptRequest.newBuilder()
                    .setNewCapsule(reproxy.getNewCapsule()).setBPrivate(pemFileJson)
                    .setCipherText(ByteString.copyFrom(reproxy.getCipherText()))
                    .setPubX(reproxy.getPublicX()).build();

            ReCrypt.DeCryptResponse deCryptResponse = reCryptBlockingStub.deCrypt(deCryptRequest);

            if(Objects.isNull(deCryptResponse)){
                return new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, "服务器错误，解密失败！");
            }else {

                if(Objects.equals(200,deCryptResponse.getCode())){
                    String plainText = deCryptResponse.getPlainText();
                    ShareDataBO shareDataBO = JSON.parseObject(plainText, ShareDataBO.class);
                    PersonMainInfo personMainInfo = personSolService.select(shareDataBO.getPersonId());
                    BlockMainInfo blockMainInfo = clientService.getBlockInfoByTransactionHash(shareDataBO.getPersonTransactionHash());
                    List<EducationVO> educationVOList = getEducationVOList(shareDataBO.getEduList());
                    channel.isShutdown();
                    return new ResponseBean(ResultCode.SUCCESS, new DataVO(new BasicVO(personMainInfo, blockMainInfo), educationVOList));
                }else {
                    return  new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, "服务器错误，解密失败！msg:"+deCryptResponse.getMsg());
                }
            }
        } catch (Exception e) {
            return  new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, "服务器错！msg:"+e.getMessage());

        }

    }





    /**
     * 封装教育数据视图列表
     *
     * @param eduList 教育数据key列表
     * @return 封装的教育视图列表
     */
    private List<EducationVO> getEducationVOList(List<String> eduList) {
        List<EducationVO> resList = new ArrayList<>(eduList.size());
        for (String eduKey : eduList) {
            String[] keys = eduKey.split(";");
            // 保证传入的数据列表：教育经历ID，教育经历交易hash，课程数据交易hash，成就数据交易hash
            // 数据初始化位置为个人端-教育经历控制层-教育信息分享接口及其内部服务实现
            String eduId = keys[0];
            String eduTxHash = keys[1];

            // 不展示课程和成就所在区块信息

            EducationVO educationVO = new EducationVO(
                    eduRecordSolService.select(eduId),
                    clientService.getBlockInfoByTransactionHash(eduTxHash),
                    getChainCourseList(eduId),
                    getChainAchieveList(eduId));
            resList.add(educationVO);
        }

        return resList;
    }

    /**
     * 封装课程数据
     *
     * @param eduId 教育经历ID
     * @return 课程列表
     */
    private List<ChainCourse> getChainCourseList(String eduId) {
        CourseMainInfo courseMainInfo = courseSolService.select(eduId);
        if (Objects.isNull(courseMainInfo)) {
            return null;
        }

        List<String> nameList = courseMainInfo.getNameList();
        List<String> creditList = courseMainInfo.getCreditList();
        List<String> certifyFileList = courseMainInfo.getCertifyFileList();
        List<ChainCourse> resList = new ArrayList<>(nameList.size());

        for (int i = 0; i < nameList.size(); i++) {
            resList.add(new ChainCourse(nameList.get(i), creditList.get(i), certifyFileList.get(i)));
        }

        return resList;
    }

    /**
     * 封装成就数据
     *
     * @param eduId 教育经历ID
     * @return 课程列表
     */
    private List<ChainAchieve> getChainAchieveList(String eduId) {
        AchieveMainInfo achieveMainInfo = achieveSolService.select(eduId);
        if (Objects.isNull(achieveMainInfo)) {
            return null;
        }

        List<String> titleList = achieveMainInfo.getTitleList();
        List<String> acquireTimeList = achieveMainInfo.getAcquireTimeList();
        List<String> certifyFileList = achieveMainInfo.getCertifyFileList();
        List<ChainAchieve> resList = new ArrayList<>(titleList.size());

        for (int i = 0; i < titleList.size(); i++) {
            resList.add(new ChainAchieve(titleList.get(i), acquireTimeList.get(i), certifyFileList.get(i)));
        }

        return resList;
    }



    /**
     * 第三方注册
     */
    @GetMapping("verifier/register/{idCard}")
    public ResponseBean verifierRegister(@PathVariable("idCard") String idCard) {
        keyService.generateOneKey(idCard,1);
        return new ResponseBean(ResultCode.SUCCESS,"注册成功");
    }


    public static void main(String[] args) {

//        String idCard = "556";
//        THISverifierRegister()

//        System.out.println(aPublic);


//         String host = "202.193.59.211";//服务器
//         int serverPort = 8888;//服务端口号
//
//        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, serverPort)
//                .usePlaintext().build();
//        ReCryptGrpc.reCryptBlockingStub reCryptBlockingStub = ReCryptGrpc.newBlockingStub(channel);
//        ReCrypt.generateKeysRequest request = ReCrypt.generateKeysRequest
//                .newBuilder().setName("a").build();
//        System.out.println("客户端发送的请求内容为：" + request.getName());
//        //客户端发送请求
//        ReCrypt.generateKeysResponse response = reCryptBlockingStub.generateKeys(request);
//        Integer code = response.getCode();
//        String aPublic = response.getAPublic();
//        String aPrivate = response.getAPrivate();
//        String msg = response.getMsg();
//
//        System.out.println(aPublic);
//
//
//        byte[] decode = Base64.getDecoder().decode(aPublic);
//        byte[] decodeA = Base64.getDecoder().decode(aPrivate);
//
//        aPublic = new String(decode);
//        aPrivate = new String(decodeA);
//
//        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
//        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPublic:" + aPublic);
//        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPrivate:" + aPrivate);
//        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);
//

//        aPublic = new String(decode);
//        aPrivate = new String(decodeA);
//
//        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
//        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPublic:" + aPublic);
//        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPrivate:" + aPrivate);
//        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);


        //判断用户是否有重复的



        //调用grpc接口





        //数据库记录公私钥
//        idCard
//        aPublic
//        aPrivate
//        type:1


        //私钥生成证书下载
        //        aPublic
//        aPrivate



    }


//    //文件下载
//    public Result download(HttpServletResponse response,
//                           String content) {
//        String host = "/ip4/127.0.0.1/tcp/5006";
//        Result<String> file = new Result<>();
//        try {
//            IPFS ipfs = new IPFS(host);
//            Multihash multihash = Multihash.fromBase58(hash);
//            byte[] result = ipfs.cat(multihash);
//            response.setContentType("application/vnd.ms-word;charset=utf-8");
//            response.setHeader("Content-Disposition",
//                    "attachment;filename=\"" + new String((hash+"."+type).getBytes("gb2312"), "ISO8859-1"));
//            ServletOutputStream os = response.getOutputStream();
//            os.write(result);
//            os.flush();
//            os.close();
//            System.out.println("下载成功");
//            return file;
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("upload fail ,msg ={}",e.getMessage());
//            return Result.error("下载失败");
//        }
//    }



 }




