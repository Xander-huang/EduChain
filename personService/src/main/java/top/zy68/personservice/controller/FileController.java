package top.zy68.personservice.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zy68.personservice.exception.ClientBusinessException;
import top.zy68.personservice.exception.InternalBusinessException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.*;

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

    /**
     * 获取证书文件
     *
     * @param fileName 文件名
     * @param response HttpServletResponse
     */
    @ApiOperation("获取用户的证书文件")
    @GetMapping("/downloadFile")
    public void getCertifyFile(@RequestParam("fileName") @NotEmpty String fileName, HttpServletResponse response) {
        File file = new File(filePath + File.separator + fileName);
        if (!file.exists()) {
            log.info("文件：{} 不存在", fileName);
            throw new ClientBusinessException("文件：" + fileName + "不存在.");
        }

        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        byte[] buff = new byte[1024];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            log.error("文件下载失败：{}", e.getMessage());
            throw new InternalBusinessException("文件：" + fileName + "下载失败.");
        }

        log.info("文件下载成功.");
    }
}
