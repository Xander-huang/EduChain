package top.zy68.allianceservice.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.allianceservice.service.StudentService;

import javax.annotation.Resource;

/**
 * @ClassName StudentServiceImplTest
 * @Description TODO
 * @Author Sustart
 * @Date2022/4/6 14:10
 * @Version 1.0
 **/
@SpringBootTest
class StudentServiceImplTest {

    @Resource
    StudentService studentService;

    @Test
    void generateStuId() {
    }

    @Test
    void addStudent() {
    }

    @Test
    void deleteStudent() {
    }

    @Test
    void insertStudentBatch() {
    }

    @Test
    void changeStudentMajor() {
    }

    @Test
    void getAllStudents() {
    }

    @Test
    void getMajorKeyValueMap() {
    }

    // @Test
    void uplinkStuEduInfo() {
        studentService.uplinkStuEduInfo(null);
    }
}