package top.zy68.personservice.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;
import top.zy68.fbChainService.service.PersonSolService;
import top.zy68.personservice.enums.enums.EncryptTypes;
import top.zy68.personservice.exception.BaseException;
import top.zy68.personservice.pojo.uservo.RspUserInfoVo;
import top.zy68.personservice.utils.CommonUtils;

import javax.annotation.Resource;

import static top.zy68.personservice.enums.enums.CodeMessageEnums.*;
import static top.zy68.personservice.enums.enums.CodeMessageEnums.PRIVATEKEY_NOT_SUPPORT_TRANSFER;

// import top.zy68.fbChainService.model.chainMainInfo.PersonMainInfo;
// import top.zy68.fbChainService.service.PersonSolService;

/**
 * @ClassName EducationRecordServiceTest
 * @Description TODO
 * @Author Sustart
 * @Date2022/3/6 16:23
 * @Version 1.0
 **/
@SpringBootTest
class EducationRecordServiceTest {
    // @Resource
    // EduService educationRecordService;

    // @Test
    // void queryByIdNumber() {
    //     List<EducationRecord> list = educationRecordService.queryByIdNumber("124565577795320154");
    //     System.out.println(list.size());
    //     EducationRecord educationRecord1 = list.get(0);
    //     EducationRecord educationRecord2 = list.get(1);
    //     System.out.println(educationRecord1.toString());
    //     System.out.println(educationRecord2.toString());
    // }


}