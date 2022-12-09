package top.zy68.allianceservice.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.easyexcel.tableField.CourseExcel;
import top.zy68.allianceservice.service.CourseService;
import top.zy68.fbChainService.entity.Course;

import java.util.List;

/**
 * @ClassName courseExcelListener
 * @Description courseExcel的readListener
 * @Author Sustart
 * @Date2022/2/18 16:01
 * @Version 1.0
 **/
@Slf4j
public class CourseExcelListener implements ReadListener<CourseExcel> {
    /**
     * 每次缓存的数据数量
     */
    private static final int BATCH_COUNT = 20;
    private final String majorId;
    private final CourseService courseService;
    /**
     * 批量生成新的课程ID，数量为 BATCH_COUNT
     */
    private List<String> cachedCourseIdList;
    /**
     * 缓存的数据
     */
    private List<Course> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 传入引用参数
     *
     * @param courseService courseService
     * @param majorId       联盟点编号
     */
    public CourseExcelListener(CourseService courseService, String majorId) {
        this.majorId = majorId;
        this.courseService = courseService;
        this.cachedCourseIdList = courseService.generateBatchCourseId(majorId, BATCH_COUNT);
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    一行数据
     * @param context AnalysisContext
     */
    @Override
    public void invoke(CourseExcel data, AnalysisContext context) {
        if (ObjectUtils.isEmpty(data.getCourseName()) || ObjectUtils.isEmpty(data.getCredit())) {
            log.info("读取到空数据，读取下一行.");
            return;
        }
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        //CourseId使用批量生成的ID
        Course course = new Course()
                .setCourseId(cachedCourseIdList.get(cachedDataList.size()))
                .setName(data.getCourseName())
                .setCredit(data.getCredit());
        cachedDataList.add(course);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理缓存数据，并重新生成一批新专业ID
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            cachedCourseIdList = courseService.generateBatchCourseId(majorId, BATCH_COUNT);
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
    }

    /**
     * 存储数据到数据库
     */
    private void saveData() {
        log.info("当前一共解析了 {} 条数据，开始存储数据库！", cachedDataList.size());
        courseService.insertCourseBatch(cachedDataList);
        log.info("存储数据库成功！");
    }
}
