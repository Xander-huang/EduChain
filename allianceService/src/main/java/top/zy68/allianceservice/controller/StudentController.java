package top.zy68.allianceservice.controller;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.zy68.allianceservice.api.ResponseBean;
import top.zy68.allianceservice.api.ResultCode;
import top.zy68.allianceservice.common.Constants;
import top.zy68.allianceservice.easyexcel.listener.StudentExcelListener;
import top.zy68.allianceservice.easyexcel.tableField.StudentExcel;
import top.zy68.allianceservice.exception.ClientBusinessException;
import top.zy68.allianceservice.service.StudentService;
import top.zy68.allianceservice.util.IdUtil;
import top.zy68.allianceservice.util.JwtUtil;
import top.zy68.fbChainService.dao.PersonDao;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName StudentController
 * @Description 学生管理
 * @Author Sustart
 * @Date2022/3/15 13:31
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("student")
public class StudentController {

    @Resource
    private StudentService studentService;
    @Resource
    private PersonDao personDao;

    /**
     * 获取学生信息
     *
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @RequiresRoles({"director"})
    @GetMapping("getAllStudents")
    public ResponseBean getAllStudents(HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        return new ResponseBean(ResultCode.SUCCESS, studentService.getAllStudents(IdUtil.getNodeId(account)));
    }

    /**
     * 添加一个学生
     *
     * @param personId 身份证号
     * @param majorId  专业编号
     * @return ResponseBean
     */
    @RequiresRoles({"director"})
    @PostMapping("addStudent")
    public ResponseBean addStudent(@RequestParam("personId") String personId,
                                   @RequestParam("majorId") String majorId) {
        return new ResponseBean(ResultCode.SUCCESS, studentService.addStudent(personId, majorId));
    }


    /**
     * 删除一个学生
     *
     * @param stuId   学生学号
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @RequiresRoles({"director"})
    @PostMapping("delStudent")
    public ResponseBean deleteStudent(@RequestParam("stuId") String stuId,
                                      HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        if (Constants.isIllegal(account, stuId)) {
            throw new ClientBusinessException("操作非法，无权限.");
        }
        return new ResponseBean(ResultCode.SUCCESS, studentService.deleteStudent(stuId));
    }

    /**
     * 更改学生专业
     *
     * @param stuId   身份证号
     * @param majorId 新专业编号
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @RequiresRoles({"director"})
    @PostMapping("updateStudent")
    public ResponseBean updateStudent(@RequestParam("stuId") String stuId,
                                      @RequestParam("majorId") String majorId,
                                      HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        if (Constants.isIllegal(account, stuId) || Constants.isIllegal(account, majorId)) {
            throw new ClientBusinessException("操作非法，无权限.");
        }
        return new ResponseBean(ResultCode.SUCCESS, studentService.changeStudentMajor(stuId, majorId));
    }

    /**
     * 批量添加学生
     *
     * @param file    新增专业的Excel
     * @param request HttpServletRequest
     * @return ResponseBean
     */
    @RequiresRoles({"director"})
    @PostMapping("addStudents")
    @ResponseBody
    public ResponseBean studentExcelImport(MultipartFile file, HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        if (file.isEmpty()) {
            throw new ClientBusinessException("未接收到文件，请重试.");
        }
        log.info("批量导入学生文件：" + file.getOriginalFilename());
        try {
            EasyExcel.read(file.getInputStream(), StudentExcel.class, new StudentExcelListener(studentService, personDao, IdUtil.getNodeId(account))).sheet().doRead();
        } catch (IOException e) {
            log.error("文件读取异常{}", e.getMessage());
            throw new ClientBusinessException("文件读取异常，请检查文件格式");
        }
        return new ResponseBean(ResultCode.SUCCESS, null);
    }

    /**
     * 学生信息上链
     *
     * @param stuIdList 学生学号
     * @param request   HttpServletRequest
     * @return ResponseBean
     */
    @RequiresRoles({"director"})
    @PostMapping("uplink")
    public ResponseBean uplinkStuEduInfo(@RequestParam("studentList") String[] stuIdList,
                                         HttpServletRequest request) {
        String account = JwtUtil.getAccount(request.getHeader(Constants.HTTP_HEADER_AUTHORIZATION));
        if (Constants.isIllegal(account, stuIdList[0])) {
            throw new ClientBusinessException("操作非法，无权限.");
        }
        return new ResponseBean(ResultCode.SUCCESS, studentService.uplinkStuEduInfo(stuIdList));
    }

    /**
     * 获取学生在联盟内的教育id
     *
     * @param personId 身份证号
     * @return ResponseBean
     */
    @RequiresRoles(value = {"director", "teacher"}, logical = Logical.OR)
    @GetMapping("/getStudentId/{personId}")
    public ResponseBean getStudentId(@PathVariable("personId") String personId) {
        return new ResponseBean(ResultCode.SUCCESS, studentService.getStudentIdAndMajor(personId));
    }
}

