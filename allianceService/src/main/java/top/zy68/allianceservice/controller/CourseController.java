package top.zy68.allianceservice.controller;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.easyexcel.listener.CourseExcelListener;
import top.zy68.allianceservice.easyexcel.tableField.CourseExcel;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.service.CourseService;
import top.zy68.fbChainService.entity.Course;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName CourseController
 * @Description 课程管理
 * @Author Sustart
 * @Date2022/3/15 13:31
 * @Version 1.0
 **/
@Validated
@Slf4j
@RequiresRoles({"director"})
@RestController
@RequestMapping("course")
public class CourseController {

    @Resource
    CourseService courseService;

    /**
     * 批量新增课程
     *
     * @param majorId 专业编号
     * @param file    新增课程的Excel
     * @return ResponseBean
     */
    @PostMapping("addCourses")
    @ResponseBody
    public ResponseBean courseExcelImport(@NotEmpty String majorId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new ClientBusinessException("文件不能为空.");
        }
        log.info("接收到文件:" + file.getOriginalFilename());
        try {
            EasyExcel.read(file.getInputStream(), CourseExcel.class, new CourseExcelListener(courseService, majorId)).sheet().doRead();
        } catch (IOException e) {
            log.info("文件读取异常{}", e.getMessage());
            throw new ClientBusinessException("文件读取异常，请检查文件.");
        }
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 通过专业号查询对应专业下的课程
     *
     * @param majorId 专业编号
     * @return ResponseBean
     */
    @GetMapping("getAllCourses/{majorId}")
    @ResponseBody
    public ResponseBean queryCoursesByMajorId(@PathVariable("majorId") @NotEmpty String majorId) {
        List<Course> courses = courseService.queryCourseSetByMajorId(majorId);
        return new ResponseBean(ResultCode.SUCCESS, courses);
    }

    /**
     * 添加课程
     *
     * @param majorId 专业Id
     * @param name    课程名
     * @param credit  课程学分
     * @return ResponseBean
     */
    @PostMapping("addCourse")
    @ResponseBody
    public ResponseBean insertCourse(@NotEmpty String majorId,
                                     @NotEmpty String name,
                                     @Range(min = 1, max = 7, message = "学分取值范围为 1-7") int credit) {
        return new ResponseBean(ResultCode.SUCCESS, courseService.insertCourse(majorId, name, (short) credit));
    }

    /**
     * 删除课程
     *
     * @param courseId 课程Id
     * @return ResponseBean
     */
    @PostMapping("delCourse")
    @ResponseBody
    public ResponseBean deleteCourse(@NotEmpty String courseId) {
        return new ResponseBean(ResultCode.SUCCESS, courseService.deleteCourse(courseId));
    }

    /**
     * 更新课程信息
     *
     * @param courseId 课程Id
     * @param name     课程名
     * @param credit   课程学分
     * @return ResponseBean
     */
    @PostMapping("updateCourse")
    @ResponseBody
    public ResponseBean updateCourse(@NotEmpty String courseId,
                                     @NotEmpty String name,
                                     @Range(min = 1, max = 7, message = "课程学分取值:1~7") short credit) {
        courseService.updateCourse(new Course(courseId, name, credit));
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * @param stuId    学号
     * @param courseId 课程编号
     * @return ResponseBean
     */
    @PostMapping("addStudentCourse")
    @ResponseBody
    public ResponseBean insertCourseByStu(@NotEmpty String stuId, @NotEmpty String courseId) {
        courseService.insertCourseByStu(stuId, courseId);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * @param stuId    学号
     * @param courseId 课程编号
     * @return ResponseBean
     */
    @PostMapping("delStudentCourse")
    @ResponseBody
    public ResponseBean deleteCourseByStu(@NotEmpty String stuId, @NotEmpty String courseId) {
        courseService.deleteCourseByStu(stuId, courseId);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }
}

