package top.zy68.allianceservice.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.allianceservice.service.CourseService;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName CourseServiceImplTest
 * @Description
 * @Author Sustart
 * @Date2022/5/20 15:27
 * @Version 1.0
 **/
@SpringBootTest
class CourseServiceImplTest {

    @Resource
    CourseService courseService;

    @Test
    void generateBatchCourseId() {
        List<String> list = courseService.generateBatchCourseId("28579698", 20);
        System.out.println(list);
    }
}