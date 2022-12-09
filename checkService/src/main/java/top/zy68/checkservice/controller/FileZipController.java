/*
package top.zy68.checkservice.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.zy68.checkservice.common.ResponseBean;
import top.zy68.checkservice.common.ResultCode;
import top.zy68.checkservice.model.pojo.ChainAchieve;
import top.zy68.checkservice.model.pojo.ChainCourse;
import top.zy68.checkservice.model.pojo.ShareDataBO;
import top.zy68.checkservice.model.vo.BasicVO;
import top.zy68.checkservice.model.vo.DataVO;
import top.zy68.checkservice.model.vo.EducationVO;
import top.zy68.fbChainService.entity.CheckKeyDto;
import top.zy68.fbChainService.entity.Key;
import top.zy68.fbChainService.entity.Reproxy;
import top.zy68.fbChainService.entity.UserCaptcha;
import top.zy68.fbChainService.grpc.recrypt.ReCrypt;
import top.zy68.fbChainService.grpc.recrypt.ReCryptGrpc;
import top.zy68.fbChainService.model.chainMainInfo.AchieveMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.BlockMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.CourseMainInfo;
import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;
import top.zy68.fbChainService.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static top.zy68.fbChainService.common.ApiConst.GrpcConst.host;
import static top.zy68.fbChainService.common.ApiConst.GrpcConst.serverPort;


@RestController
@RequestMapping("/file")
public class FileZipController {
    @Resource
    private IFileService fileService;

    @GetMapping("/indirectDown")
    public void indirectDownFile(@RequestParam("fileId") String fileId,HttpServletResponse response) throws IOException {
        fileService.indirectDownFile(fileId,response);
    }

    @GetMapping("/directDown")
    public void directDownFile(HttpServletResponse response) throws IOException {

        String s1 = new String("哈哈哈哈哈哈");
        String s2 = new String("xixixixiii");
        List<byte[]> objects = new ArrayList<>();
        objects.add(s1.getBytes());
        objects.add(s2.getBytes());
        fileService.directDownFile(objects,response);
    }

}

*/
