package top.zy68.fbChainService.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;

public  class FileUtil {


    public void FileReadSecond(String filepath) {
//        File src=new File("src/IO/abc.txt");
//        try {
//            InputStream is=new FileInputStream(src);
//            int data1=is.read();
//            int data2=is.read();
//            int data3=is.read();
//            is.close();
//            System.out.println((char)data1);
//            System.out.println((char)data2);
//            System.out.println((char)data3);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//            }


        File src = new File(filepath);
        InputStream is = null;
        try {
            is = new FileInputStream(src);
            byte[] flash = new byte[1024 * 10];
            int len = -1;
            while ((len = is.read(flash)) != -1) {
                String str = new String(flash, 0, len);
                System.out.println(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static  String  handPemFile(MultipartFile file) throws Exception{

        String pemJson  = "";
        try{

                InputStreamReader read = new InputStreamReader( file.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineText = null;
                int i = 0;
                // 按行读取文件并打印,如果需要对内容进行操作可以在这里进行
                while((lineText = bufferedReader.readLine())!=null){
                    if(Objects.equals(1,i)){
                        pemJson = lineText;
                    }
                    i++;
                }
             System.out.println("pemJson==========="+pemJson);

        } catch(IOException e){
            e.printStackTrace();
            throw e;
        }

        return pemJson;
    }
}
