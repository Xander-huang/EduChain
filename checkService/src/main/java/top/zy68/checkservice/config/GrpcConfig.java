/*
package top.zy68.checkservice.config;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.zy68.checkservice.grpc.recrypt.ReCryptGrpc;

@Configuration
public class GrpcConfig {

    private static final String host = "202.193.59.211";//服务器
    private static final int serverPort = 8888;//服务端口号


    @Bean("reCryptBlockingStub")
    public ReCryptGrpc.reCryptBlockingStub getGrpcStu(){
        //创建一个通讯管道channel,构造器传入定义的服务IP和端口,usePlaintext()表示一个传输文本的通道
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, serverPort)
                .usePlaintext().build();
        ReCryptGrpc.reCryptBlockingStub reCryptBlockingStub = ReCryptGrpc.newBlockingStub(channel);

        return reCryptBlockingStub;

    }
}
*/
