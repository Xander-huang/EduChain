package top.zy68.fbChainService.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IFileService {


    void indirectDownFile(String fileId, HttpServletResponse response) throws IOException;

    void downZipFile(List<byte[]>  fileBytes, HttpServletResponse response) throws IOException;

}
