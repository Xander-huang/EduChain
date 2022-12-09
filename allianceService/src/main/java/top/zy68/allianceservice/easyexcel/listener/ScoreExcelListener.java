package top.zy68.allianceservice.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.easyexcel.tableField.ScoreExcel;
import top.zy68.allianceservice.exception.NormalBusinessException;
import top.zy68.allianceservice.pojo.UpdateScoreBO;
import top.zy68.allianceservice.service.CourseScoreService;
import top.zy68.fbChainService.dao.StudentDao;
import top.zy68.fbChainService.entity.Student;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName ScoreExcelListener
 * @Description scoreExcel的readListener
 * @Author Sustart
 * @Date2022/2/18 16:01
 * @Version 1.0
 **/

@Slf4j
public class ScoreExcelListener implements ReadListener<ScoreExcel> {
    /**
     * 每次缓存的数据数量
     */
    private static final int BATCH_COUNT = 20;
    private final StudentDao studentDao;
    private final CourseScoreService courseScoreService;
    private final List<String> failedList;
    /**
     * 缓存的数据
     */
    private List<UpdateScoreBO> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


    /**
     * @param studentDao         教育经历服务
     * @param courseScoreService 课程成绩服务
     */
    public ScoreExcelListener(CourseScoreService courseScoreService, StudentDao studentDao) {
        this.studentDao = studentDao;
        this.courseScoreService = courseScoreService;
        failedList = new LinkedList<>();
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    一行数据
     * @param context AnalysisContext
     */
    @Override
    public void invoke(ScoreExcel data, AnalysisContext context) {
        // 要求的所需要的每列、每行数据都不能有缺
        if (ObjectUtils.isEmpty(data.getPersonId()) || ObjectUtils.isEmpty(data.getMajorId()) || ObjectUtils.isEmpty(data.getCourseId()) || ObjectUtils.isEmpty(data.getCourseScore())) {
            log.info("读取到空数据，读取下一行.");
            return;
        }
        log.info("解析到一条数据:{}", JSON.toJSONString(data));

        // 通过身份证号和专业编号确定到学生
        Student student = studentDao.queryStuByPersonIdAndMajorId(data.getPersonId(), data.getMajorId());
        if (ObjectUtils.isEmpty(student)) {
            failedList.add("学生（身份证号：" + data.getPersonId() + " 专业编号：" + data.getMajorId() + "）不存在");
            return;
        }
        cachedDataList.add(new UpdateScoreBO(student, data.getCourseId(), data.getCourseScore()));

        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 整个excel解析结束后执行
     *
     * @param context AnalysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 确保最后遗留的数据（数据量不足Batch_COUNT）也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
        if (!failedList.isEmpty()) {
            throw new NormalBusinessException(failedList.toString());
        }
    }

    /**
     * 存储数据到数据库
     */
    private void saveData() {
        log.info("当前一共解析了 {} 条数据，开始存储数据库！", cachedDataList.size());
        courseScoreService.updateScoreBatch(cachedDataList, failedList);
        log.info("存储数据库成功！");
    }
}
