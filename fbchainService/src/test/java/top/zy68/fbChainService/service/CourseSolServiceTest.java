package top.zy68.fbChainService.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.fbChainService.model.chainMainInfo.CourseMainInfo;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CourseSolServiceTest
 * @Description
 * @Author Sustart
 * @Date2022/4/7 14:25
 * @Version 1.0
 **/
@SpringBootTest
class CourseSolServiceTest {

    @Resource
    CourseSolService courseSolService;

    // @Test
    void insert() {
        CourseMainInfo courseMainInfo = new CourseMainInfo();
        courseMainInfo.setEduId("22229123001");
        int length = 2;

        List<String> nameList = new ArrayList<>(length);
        List<String> creditList = new ArrayList<>(length);
        List<String> fileList = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            nameList.add("高等数学" + i);
            creditList.add("5" + i);
            fileList.add("file" + i + ".pdf");
        }
        courseMainInfo.setNameList(nameList);
        courseMainInfo.setCreditList(creditList);
        courseMainInfo.setCertifyFileList(fileList);

        String hash = courseSolService.insert(courseMainInfo);
        System.out.println(hash);
    }

    @Test
    void select() {
        String eduId = "22229123001";
        CourseMainInfo mainInfo = courseSolService.select(eduId);

        List<String> nameList = mainInfo.getNameList();
        List<String> creditList = mainInfo.getCreditList();
        List<String> fileList = mainInfo.getCertifyFileList();

        System.out.println(mainInfo.getEduId());
        for (int i = 0; i < nameList.size(); i++) {
            System.out.println(nameList.get(i));
            System.out.println(creditList.get(i));
            System.out.println(fileList.get(i));
        }
    }
}