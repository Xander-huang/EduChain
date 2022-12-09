
package top.zy68.fbChainService.grpc.recrypt;


import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Base64;

import static top.zy68.fbChainService.common.ApiConst.GrpcConst.host;
import static top.zy68.fbChainService.common.ApiConst.GrpcConst.serverPort;

public class CryptClient {


    public static void main(String[] args) {
        //创建一个通讯管道channel,构造器传入定义的服务IP和端口,usePlaintext()表示一个传输文本的通道
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, serverPort)
                .usePlaintext().build();
        try {

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
            System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
            System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPublic:" + aPublic);
            System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,aPrivate:" + aPrivate);
            System.out.println("A生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);
            //===========================B生成密钥=========================

            //客户端发送请求
            ReCrypt.generateKeysResponse responseB = reCryptBlockingStub.generateKeys(request);
            Integer codeB = responseB.getCode();
            String bPublic = responseB.getAPublic();
            String bPrivate = responseB.getAPrivate();
            System.out.println("B生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,codeB:" + codeB);
            System.out.println("B生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,bPublic:" + bPublic);
            System.out.println("B生成密钥的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,bPrivate:" + bPrivate);
            System.out.println("B加密密文的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);

            byte[] decodedBytes = Base64.getDecoder().decode(aPublic);
            aPublic = new String(decodedBytes);
            byte[] decodedBytes2 = Base64.getDecoder().decode(aPrivate);
            aPrivate = new String(decodedBytes2);
            byte[] decodedBytes3 = Base64.getDecoder().decode(bPublic);
            bPublic = new String(decodedBytes3);
            byte[] decodedBytes4 = Base64.getDecoder().decode(bPrivate);
            bPrivate = new String(decodedBytes4);


            //==========================A加密密文===========================
            String m = "我是代理重加密";
            ReCrypt.ACryptRequest aCryptRequest = ReCrypt.ACryptRequest
                    .newBuilder().setAPublicKey(aPublic).setPlainText(m).build();
            System.out.println("明文消息===================== m："+m);
            ReCrypt.ACryptResponse aCryptResponse = reCryptBlockingStub.cryptText(aCryptRequest);
            code = aCryptResponse.getCode();
            ByteString ciphereText = aCryptResponse.getCiphereText();
            String sl = ciphereText.toStringUtf8();
            byte[] bytes = ciphereText.toByteArray();


            ciphereText = ByteString.copyFrom(bytes);

            System.out.println("加密后的密文的byte ciphereText："+ciphereText);
            msg = aCryptResponse.getMsg();
            String capsule = aCryptResponse.getCapsule();


            System.out.println("A加密密文的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
            System.out.println("A加密密文的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,ciphereText:" + ciphereText);
            System.out.println("A加密密文的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,capsule:" + capsule);
            System.out.println("A加密密文的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);

            //=========================server重加密========================
            ReCrypt.ReCryptRequest reCryptRequest = ReCrypt.ReCryptRequest.newBuilder().setAPrivate(aPrivate)
                    .setCapsule(capsule).setBPublicKey(bPublic).build();
            ReCrypt.ReCryptResponse reCryptResponse = reCryptBlockingStub.reCrypt(reCryptRequest);
            code = reCryptResponse.getCode();
            String newCapsule = reCryptResponse.getNewCapsule();
            String pubX = reCryptResponse.getPubX();
            msg = reCryptResponse.getMsg();
            System.out.println("server重加密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
            System.out.println("server重加密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,newCapsule:" + newCapsule);
            System.out.println("server重加密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,pubX:" + pubX);
            System.out.println("server重加密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);

            //=========================B来解密========================
            ReCrypt.DeCryptRequest deCryptRequest = ReCrypt.DeCryptRequest.newBuilder().setNewCapsule(newCapsule).setBPrivate(bPrivate)
                    .setCipherText(ciphereText).setPubX(pubX).build();

            ReCrypt.DeCryptResponse deCryptResponse = reCryptBlockingStub.deCrypt(deCryptRequest);

            code = deCryptResponse.getCode();
            String plainText = deCryptResponse.getPlainText();
            msg = deCryptResponse.getMsg();
            System.out.println("B来解密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
            System.out.println("B来解密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。解密出来的明文为plainText:" + plainText);
            System.out.println("B来解密的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);

            //=========================A用自己的私钥来解密========================
            ReCrypt.DeCryptOwnRequest deCryptOwnRequest = ReCrypt.DeCryptOwnRequest.newBuilder().setAPrivate(aPrivate)
                    .setCapsule(capsule).setCipherText(ciphereText).build();

            ReCrypt.DeCryptOwnResponse deCryptOwnResponse = reCryptBlockingStub.deCryptByOwnPri(deCryptOwnRequest);

            code = deCryptOwnResponse.getCode();
            String plainTextByPri = deCryptOwnResponse.getPlainText();
            msg = deCryptOwnResponse.getMsg();
            System.out.println("A用自己的私钥来解密密文的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,code:" + code);
            System.out.println("A用自己的私钥来解密密文的api,客户端接收服务器消息成功。。。。。。。。。。。。。。解密出来的明文为plainText:" + plainTextByPri);
            System.out.println("A用自己的私钥来解密密文的api,客户端接收服务器消息成功。。。。。。。。。。。。。。,msg:" + msg);


        } finally {
            channel.shutdown();
        }
    }
}

