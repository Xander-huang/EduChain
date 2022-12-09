package top.zy68.allianceservice.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName NodeDataVO
 * @Description 节点数据封装视图
 * student为联盟内学生数量
 * major为联盟内专业数量
 * course:联盟内课程数量
 * uplinked联盟内已经上链学生数量
 * annualEnrollment为联盟每年入学人数
 * annualGraduation为联盟每年上链（毕业）人数
 * annualMajor为每年开设专业数量
 * annualCourse为每年开始课程数量
 * majorAverage为当前年份专业平均分排行前8，由高到低排列
 * majorNum为当前年份专业人数数量前8，由高到低排列
 * education为当前年份学生总体成绩，分为四类：平均成绩>90, 平均成绩>80, 平均成绩>60, 平均成绩<60,
 * @Author Sustart
 * @Date2022/4/11 8:56
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeDataVO {
    private Integer student;
    private Integer major;
    private Integer course;
    private Integer uplinked;
    private IntListVO annualEnrollment;
    private IntListVO annualGraduation;
    private IntListVO annualMajor;
    private IntListVO annualCourse;

    private StrFloatMapVO majorAverage;
    private StrIntMapVO majorNum;
    private List<Integer> education;
}
