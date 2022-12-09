package top.zy68.allianceservice.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.exception.InternalBusinessException;
import top.zy68.allianceservice.service.CourseScoreService;
import top.zy68.allianceservice.service.EduRecordService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.*;
import java.util.UUID;

/**
 * @ClassName FileController
 * @Description 文件上传下载接口
 * @Author Sustart
 * @Date2022/4/16 21:19
 * @Version 1.0
 **/
@Slf4j
@RestController
public class FileController {
    @Value("${file.store.path}")
    private String filePath;
    @Resource
    EduRecordService eduRecordService;
    @Resource
    CourseScoreService courseScoreService;

    /**
     * @param eduId 教育经历ID
     * @param id    ID : 教育经历ID/课程ID
     * @param file  文件
     * @return ResponseBean
     */
    @RequiresRoles(value = {"director", "teacher"}, logical = Logical.OR)
    @PostMapping("/uploadFile")
    public ResponseBean receiveFile(@RequestParam("eduId") String eduId, @RequestParam("id") String id, MultipartFile file) {
        int eduIdLength = 40;
        int courseIdLength = 11;

        if (id.length() == eduIdLength) {
            String fileId = uploadFile(file);
            eduRecordService.updateEduRecordCertifyFile(eduId, fileId);
        } else if (id.length() == courseIdLength) {
            String fileId = uploadFile(file);
            courseScoreService.updateStuCourseCertify(eduId, id, fileId);
        } else {
            throw new ClientBusinessException("id非法.");
        }
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 新文件名
     */
    private String uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String mD5fileName;
        if (ObjectUtils.isEmpty(originalFilename)) {
            log.info("文件名不合法");
            throw new ClientBusinessException("文件名不合法.");
        }

        try {
            // 文件指纹作为文件名
            mD5fileName = DigestUtils.md5DigestAsHex(file.getBytes());
        } catch (IOException e) {
            mD5fileName = UUID.randomUUID().toString().replaceAll("-", "");
            log.warn("文件计算MD5指纹出现异常，已使用UUID替代.");
            e.printStackTrace();
        }

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        // 新文件名 = 文件指纹编码 + 文件扩展名
        String newFileName = mD5fileName + fileExtension;

        File dest = new File(filePath + File.separator + newFileName);
        dest.getParentFile().mkdirs();

        try {
            file.transferTo(dest);
            log.info("文件: \"{}\" 上传成功.", originalFilename);
        } catch (Exception e) {
            log.error("文件上传失败{}.", e.getMessage());
            throw new InternalBusinessException("文件上传失败.");
        }

        return newFileName;
    }

    /**
     * 下载证书
     *
     * @param fileName 文件名
     * @param response HttpServletResponse
     */
    @RequiresRoles({"teacher"})
    @ApiOperation("获取学生成就的证书文件")
    @GetMapping("/downloadFile/{file}")
    public void getAchieveFile(@PathVariable("file") @NotEmpty String fileName,
                               HttpServletResponse response) {

        File file = new File(filePath + '/' + fileName);
        if (!file.exists()) {
            log.info("文件：{} 不存在", fileName);
            throw new ClientBusinessException("文件不存在.");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        byte[] buff = new byte[1024];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            log.error("文件下载失败：{}", e.getMessage());
            throw new InternalBusinessException("文件下载失败.");
        }

        log.info("文件下载成功.");
    }
}
