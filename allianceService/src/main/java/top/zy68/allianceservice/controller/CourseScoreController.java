package top.zy68.allianceservice.controller;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.common.Constants;
import top.zy68.allianceservice.easyexcel.listener.ScoreExcelListener;
import top.zy68.allianceservice.easyexcel.tableField.ScoreExcel;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.pojo.dto.StuCourseScoreDTO;
import top.zy68.allianceservice.service.CourseScoreService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.allianceservice.util.JwtUtil;
import top.zy68.fbChainService.dao.StudentDao;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName CourseController
 * @Description 课程管理
 * @Author Sustart
 * @Date2022/3/15 13:31
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("courseScore")
public class CourseScoreController {

    @Resource
    CourseScoreService courseScoreService;
    @Resource
    StudentDao studentDao;

    /**
     * 获取学生及其课程成绩
     *
     * @param request
     * @return
     */
    @RequiresRoles(value = {"director", "teacher"}, logical = Logical.OR)
    @GetMapping("/getAllStuAndScore")
    public ResponseBean getAllStudentEduRecords(HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        return new ResponseBean(ResultCode.SUCCESS, courseScoreService.getAllStudentEduRecords(IdUtil.getNodeId(account)));
    }

    /**
     * 修改学生成绩
     *
     * @param stuCourseScoreDTO 接收json参数
     * @return ResponseBean
     */
    @RequiresRoles({"teacher"})
    @RequestMapping(value = "/updateStuScore", method = RequestMethod.POST, consumes = "application/json")
    public ResponseBean getAllStudentEduRecords(@RequestBody StuCourseScoreDTO stuCourseScoreDTO) {
        if (ObjectUtils.isEmpty(stuCourseScoreDTO) || ObjectUtils.isEmpty(stuCourseScoreDTO.getCourse())) {
            throw new ClientBusinessException("参数有误");
        }
        courseScoreService.updateOneStudentCourseScore(stuCourseScoreDTO);
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 通过文件批量导入多个学生的多门成绩
     *
     * @param file 含课程成绩的Excel
     * @return ResponseBean
     */
    @RequiresRoles({"teacher"})
    @PostMapping("importScoreByExcel")
    @ResponseBody
    public ResponseBean courseExcelImport(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ClientBusinessException("未接收到文件，请重试.");
        }
        log.info("导入成绩文件:{}", file.getOriginalFilename());
        try {
            EasyExcel.read(file.getInputStream(), ScoreExcel.class, new ScoreExcelListener(courseScoreService, studentDao)).sheet().doRead();
        } catch (IOException e) {
            log.error("文件读取异常{}", e.getMessage());
            throw new ClientBusinessException("文件读取异常，请检查文件格式");
        }
        return new ResponseBean(ResultCode.SUCCESS, null);
    }
}

