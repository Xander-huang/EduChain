package top.zy68.fbChainService.service.impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zy68.fbChainService.config.Mail;
import top.zy68.fbChainService.dao.KeyDao;
import top.zy68.fbChainService.dao.PersonUserDao;
import top.zy68.fbChainService.dao.ReProxyDao;
import top.zy68.fbChainService.entity.CheckKeyDto;
import top.zy68.fbChainService.entity.PersonUser;
import top.zy68.fbChainService.service.MailService;
import top.zy68.fbChainService.service.ReproxyService;
import top.zy68.fbChainService.entity.Reproxy;
import top.zy68.fbChainService.grpc.recrypt.ReCrypt;
import top.zy68.fbChainService.grpc.recrypt.ReCryptGrpc;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static top.zy68.fbChainService.common.ApiConst.GrpcConst.host;
import static top.zy68.fbChainService.common.ApiConst.GrpcConst.serverPort;


/**
 * @ClassName ReproxyService
 * @Description ReproxyService
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("reproxyService")
public class ReproxyServiceImpl implements ReproxyService {


    @Resource
    ReProxyDao reProxyDao;

    @Resource
    Mail mail;

    @Resource
    MailService mailService;

    @Resource
    KeyDao keyDao;

    @Resource
    PersonUserDao personUserDao;

    @Value("${email.ip}")
    String ipAddress;

    @Override
    public Reproxy selectByCaptcha(String captcha, String receiverId) {
        Reproxy reproxy = reProxyDao.selectByCaptcha(captcha,receiverId);

        return reproxy;
    }

    @Override
    public Reproxy selectById(Integer id) {
        reProxyDao.selectById(id);
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insertReproxy(Reproxy reproxy) {

        Integer id = reProxyDao.insertReproxy(reproxy);
        return id;

    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateReproxy(Reproxy reproxy) {

        reProxyDao.updateReproxy(reproxy);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void authorize(String captcha, CheckKeyDto keyDto) {



    }

    @Override
    public void sendEmailAuthorize(String email, Integer id,String senderName) throws Exception{

        String text = "亲爱的同学，您好：\n"
                +senderName +" " + "向您申请了一份授权请求！"
                + "\n点击下方链接跳转页面进行同意授权或拒绝授权："
                + "\n页面地址：http://"+ipAddress + ":8080/authorize/"+id;

        sendAuthorizeResByEmail(email,text);



    }

    @Override
    public void sendEmailAuthorize2(String email, Integer id,String senderName) throws Exception{

        String text = "EduChain proxy re-encryption authorization：\n"+
                "-----------------------------------------------------\n"+
                "Dear student Huang Xinzhe：\n"
                +senderName +" " + "has applied for an authorization request from you!！"
                + "\nPlease click the link below to jump to the page to agree or deny authorization："
                + "\nPage address：http://"+ipAddress + ":8080/authorize/"+id;

        sendAuthorizeResByEmail(email,text);



    }

    @Override
    public String selectEmail(String id) {
        String s = reProxyDao.selectEmail(id);

        return s;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void agree(Integer id,String priKey) throws Exception {
        //创建一个通讯管道channel,构造器传入定义的服务IP和端口,usePlaintext()表示一个传输文本的通道
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, serverPort)
                .usePlaintext().build();
        Reproxy reproxy = reProxyDao.selectById(id);
        if(Objects.isNull(reproxy)){
            throw new Exception("授权记录不存在！");
        }

        String keyById = keyDao.selectPubKeyById(reproxy.getReceiverId());
        if(Objects.isNull(keyById)){
            throw new Exception("提交授权记录的用户不存在！");
        }


        ReCryptGrpc.reCryptBlockingStub reCryptBlockingStub = ReCryptGrpc.newBlockingStub(channel);
        ReCrypt.ReCryptRequest reCryptRequest = ReCrypt.ReCryptRequest.newBuilder().setAPrivate(priKey)
                .setCapsule(reproxy.getCapsule()).setBPublicKey(keyById).build();
        ReCrypt.ReCryptResponse reCryptResponse = reCryptBlockingStub.reCrypt(reCryptRequest);
        Integer code = reCryptResponse.getCode();
        String newCapsule = reCryptResponse.getNewCapsule();
        String pubX = reCryptResponse.getPubX();
        String msg = reCryptResponse.getMsg();
        System.out.println("server重加密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
        System.out.println("server重加密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,newCapsule:" + newCapsule);
        System.out.println("server重加密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,pubX:" + pubX);
        System.out.println("server重加密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);
        channel.isShutdown();
        if(Objects.equals(200,code)){
            reproxy.setState(3);
            reproxy.setNewCapsule(newCapsule);
            reproxy.setPublicX(pubX);
            updateReproxy(reproxy);

        }else {
            throw new Exception(msg);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disagree(Integer id) throws Exception {

        Reproxy reproxy = selectById(id);
        if(Objects.isNull(reproxy)){
            throw new Exception("授权记录不存在！");
        }

        String keyById = keyDao.selectPubKeyById(reproxy.getReceiverId());
        if(Objects.isNull(keyById)){
            throw new Exception("提交授权记录的用户不存在！");
        }
        reproxy.setState(2);
        updateReproxy(reproxy);

    }

    @Override
    public List<Reproxy> list(String personId,Integer start,Integer pagNum) throws Exception {

        //todo 查出对方的姓名，连表
        List<Reproxy> reproxies = reProxyDao.selectByPersonId(personId, start, pagNum);

        return reproxies;
    }

    private void sendAuthorizeResByEmail2(String email, String text) throws Exception {

        String subject = "终身教育服务平台-数据校验授权";
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(text);
        mailService.sendMail(mail);

        if (!mail.getStatus()) {
            log.warn("Failed to send a mail to Admin: {}. Message:{}", email, mail.getError());
            throw new Exception("校验失败！");
        }
        log.info("Succeed to send a mail to Admin: " + email);
    }

    private void sendAuthorizeResByEmail(String email, String text) throws Exception {

        String subject = "EduChain proxy re-encryption authorization";
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(text);
        mailService.sendMail(mail);

        if (!mail.getStatus()) {
            log.warn("Failed to send a mail to Admin: {}. Message:{}", email, mail.getError());
            throw new Exception("校验失败！");
        }
        log.info("Succeed to send a mail to Admin: " + email);
    }
}
