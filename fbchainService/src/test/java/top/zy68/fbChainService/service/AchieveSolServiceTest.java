package top.zy68.fbChainService.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.zy68.fbChainService.model.chainMainInfo.AchieveMainInfo;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AchieveSolServiceTest
 * @Description 成就服务方法测试
 * @Author Sustart
 * @Date2022/4/4 20:46
 * @Version 1.0
 **/
@SpringBootTest
class AchieveSolServiceTest {

    @Resource
    AchieveSolService achieveSolService;

    @Test
    void select() {
        AchieveMainInfo achieveMainInfo = achieveSolService.select("9855456789");
        List<String> titles = achieveMainInfo.getTitleList();
        List<String> times = achieveMainInfo.getAcquireTimeList();
        List<String> files = achieveMainInfo.getCertifyFileList();
        System.out.println(achieveMainInfo.getEduId());
        for (int i = 0; i < titles.size(); i++) {
            System.out.println(titles.get(i));
            System.out.println(times.get(i));
            System.out.println(files.get(i));
        }
    }

    @Test
    void insert() {
        AchieveMainInfo inputBO = new AchieveMainInfo();

        List<String> titles = new ArrayList<>(1);
        List<String> times = new ArrayList<>(1);
        List<String> files = new ArrayList<>(1);
        for (int i = 0; i < 1; i++) {
            titles.add("程序设计大赛一等奖" + i);
            times.add("2020-10-0" + i);
            files.add("files.pdf" + i);
        }

        inputBO.setEduId("9855456789");
        inputBO.setTitleList(titles);
        inputBO.setAcquireTimeList(times);
        inputBO.setCertifyFileList(files);

        try {
            String transactionHash = achieveSolService.insert(inputBO);
            System.out.println(transactionHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}