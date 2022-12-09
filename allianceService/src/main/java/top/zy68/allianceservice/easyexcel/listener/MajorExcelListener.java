package top.zy68.allianceservice.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.easyexcel.tableField.MajorExcel;
import top.zy68.allianceservice.service.MajorService;
import top.zy68.fbChainService.entity.Major;

import java.util.List;

/**
 * @ClassName MajorExcelListener
 * @Description majorExcel的readListener
 * @Author Sustart
 * @Date2022/3/26 20:01
 * @Version 1.0
 **/
@Slf4j
public class MajorExcelListener implements ReadListener<MajorExcel> {

    /**
     * 每次缓存的数据数量
     */
    private static final int BATCH_COUNT = 20;
    private final Integer nodeId;
    private final MajorService majorService;
    /**
     * 批量生成新的专业ID，数量为 BATCH_COUNT
     */
    private List<String> cachedMajorIdList;
    /**
     * 缓存的数据
     */
    private List<Major> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 传入引用参数
     *
     * @param majorService majorService
     * @param nodeId       联盟点编号
     */
    public MajorExcelListener(MajorService majorService, Integer nodeId) {
        this.nodeId = nodeId;
        this.majorService = majorService;
        this.cachedMajorIdList = majorService.generateBatchMajorId(nodeId, BATCH_COUNT);
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    一行数据
     * @param context AnalysisContext
     */
    @Override
    public void invoke(MajorExcel data, AnalysisContext context) {
        if (ObjectUtils.isEmpty(data.getMajorName()) || ObjectUtils.isEmpty(data.getType())) {
            log.info("读取到空数据，读取下一行.");
            return;
        }
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        // majorId使用批量生成的ID，读取缓存数据长度值为下标的专业编号。
        Major major = new Major()
                .setMajorId(cachedMajorIdList.get(cachedDataList.size()))
                .setName(data.getMajorName())
                .setType(data.getType())
                .setGraduateCredit(0);
        cachedDataList.add(major);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理缓存数据，并重新生成一批新专业ID
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
            cachedMajorIdList = majorService.generateBatchMajorId(nodeId, BATCH_COUNT);
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
        majorService.insertMajorBatch(cachedDataList);
        log.info("存储数据库成功！");
    }
}
