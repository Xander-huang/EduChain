package top.zy68.fbChainService.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import top.zy68.fbChainService.service.IFileService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service("fileService")
public class FileServiceImpl implements IFileService {

    @Override
    public void indirectDownFile(String fileId, HttpServletResponse response) throws IOException {
        File file = new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\壁纸\\"+fileId+".jpg");
        InputStream is = new FileInputStream(file);
        byte[] bs = new byte[1024];
        int len = -1;

        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("application/msexcel;charset=utf-8");//定义输出类型
        response.setHeader("Content-Disposition","attachment;fileName=" +fileId+ ".jpg");

        while((len = is.read(bs)) != -1){
            outputStream.write(bs,0, len);
            outputStream.flush();
        }


    }

    @Override
    public void downZipFile(List<byte[]>  fileBytes, HttpServletResponse response) throws IOException {

        // 1.设置相应头，下载所有需要的文件，获取byte字节流，获取输出流对象
        response.setContentType("application/msexcel;charset=utf-8");//定义输出类型
        String downloadfile =new String(("key-file.zip").getBytes("gb2312"), "ISO8859-1");
        response.setHeader("Content-Disposition","attachment;fileName="+downloadfile);
        ServletOutputStream os = response.getOutputStream();


        /*// 2.封装zip压缩包,需要先创建文件夹
        File file = new File(tmpFilePath);
        if(!file.isDirectory()){
            file.mkdirs();
        }
*/
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream("key-file.zip"));
        InputStream is = new FileInputStream("key-file.zip");
        String fileName = "";
        for (int i = 0; i < fileBytes.size(); i++) {
            byte[] bytes = fileBytes.get(i);
            if(Objects.equals(0,i)){
                fileName = "key(user in login).pem";
            }else {
                fileName = "key(user is check code).pem";
            }

            zipOut.putNextEntry(new ZipEntry(fileName));
            zipOut.write(bytes,0,bytes.length);
            zipOut.flush();
        }
        zipOut.close();


        //3.将zip输出
        byte[] zipBytes = new byte[1024];
        int len = -1;
        while((len = is.read(zipBytes)) != -1){
            os.write(zipBytes,0,len);

        }
        os.flush();
        os.close();
        is.close();


    }



}
