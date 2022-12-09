package top.zy68.fbChainService.service.impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import top.zy68.fbChainService.grpc.recrypt.ReCrypt;
import top.zy68.fbChainService.grpc.recrypt.ReCryptGrpc;
import top.zy68.fbChainService.service.KeyService;
import top.zy68.fbChainService.dao.KeyDao;
import top.zy68.fbChainService.entity.Key;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Base64;




/**
 * @ClassName ReproxyService
 * @Description ReproxyService
 * @Author Sustart
 * @Date2022/3/16 12:10
 * @Version 1.0
 **/
@Slf4j
@Service("keyService")
public class KeyServiceImpl implements KeyService {


    private static final String host = "localhost";//服务器
    private static final int serverPort = 8888;//服务端口号

    @Resource
    KeyDao keyDao;

    @Override
    public Key selectOneByIdCard(String personId) {
        Key key = keyDao.selectOneByIdCard(personId);
        return key;
    }

    @Override
    public String generateOneKey(String idCard,Integer type) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, serverPort)
                .usePlaintext().build();
        ReCryptGrpc.reCryptBlockingStub reCryptBlockingStub = ReCryptGrpc.newBlockingStub(channel);
        ReCrypt.generateKeysRequest request = ReCrypt.generateKeysRequest
                .newBuilder().setName("a").build();
        System.out.println("客户端发送的请求内容为：" + request.getName());
        //客户端发送请求
        ReCrypt.generateKeysResponse response = reCryptBlockingStub.generateKeys(request);
        Integer code = response.getCode();
        String aPublic = response.getAPublic();
        String aPrivate = response.getAPrivate();
        String msg = response.getMsg();
        //todo 校验code
        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPublic:" + aPublic);
        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPrivate:" + aPrivate);
        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);

        System.out.println(aPublic);


        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPublic:" + aPublic);
        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPrivate:" + aPrivate);
        System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);

        //this.CreateFile(idCard,aPublicDe,aPrivateDe);
        keyDao.insertAnKey(idCard, aPublic, type);
        return  aPrivate;
    }



    //文件下载
    public void CreateFile(String idCard,String pubKey, String privKey)
    {

        String filePath = "E:/" + "idCard.txt";
        FileWriter fw = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                file.createNewFile();
            }
            fw = new FileWriter(filePath);
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write("publicKey："+pubKey+"\n\n");
            bw.write("privateKey："+privKey+"\n");
            bw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fw.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }



}








