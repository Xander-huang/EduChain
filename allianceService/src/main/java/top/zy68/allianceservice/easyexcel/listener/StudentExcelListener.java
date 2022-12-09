package top.zy68.allianceservice.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import top.zy68.allianceservice.easyexcel.tableField.StudentExcel;
import top.zy68.allianceservice.exception.FailedToImportException;
import top.zy68.allianceservice.pojo.vo.FailedToAddStuVO;
import top.zy68.allianceservice.service.StudentService;
import top.zy68.fbChainService.dao.PersonDao;
import top.zy68.fbChainService.entity.Major;
import top.zy68.fbChainService.entity.Person;
import top.zy68.fbChainService.entity.Student;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StudentExcelListener
 * @Description StudentExcel的readListener
 * @Author Sustart
 * @Date2022/2/18 16:01
 * @Version 1.0
 **/
@Slf4j
public class StudentExcelListener implements ReadListener<StudentExcel> {

    /**
     * 每次缓存的数据数量
     */
    private static final int BATCH_COUNT = 20;
    private int nodeId = 0;
    private final StudentService studentService;
    private final PersonDao personDao;
    private final List<FailedToAddStuVO> failedData = new LinkedList<>();
    /**
     * 缓存所有的专业信息，封装为KV映射形式：K=专业名称+类型 《=》V=专业编号，用于通过专业名称+类型确定专业编号
     */
    private final Map<String, Major> cachedMajorMap;
    /**
     * 缓存的数据
     */
    private List<Student> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 传入引用参数
     *
     * @param studentService studentService
     * @param personDao      个人DAO
     * @param nodeId         联盟点编号
     */
    public StudentExcelListener(StudentService studentService, PersonDao personDao, Integer nodeId) {
        this.studentService = studentService;
        this.cachedMajorMap = studentService.getMajorKeyValueMap(nodeId);
        this.personDao = personDao;
        this.nodeId = nodeId;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    一行数据
     * @param context AnalysisContext
     */
    @Override
    public void invoke(StudentExcel data, AnalysisContext context) {
        if (ObjectUtils.isEmpty(data.getPersonId()) || ObjectUtils.isEmpty(data.getMajorId()) || ObjectUtils.isEmpty(data.getMajorName())) {
            log.info("读取到空数据，读取下一行.");
            return;
        }
        log.info("解析到一条数据:{}", JSON.toJSONString(data));

        Major major = cachedMajorMap.get(data.getMajorId());
        Person person = personDao.queryPersonByPersonId(data.getPersonId());

        // 导入学生的个人信息还未注册到平台不添加
        if (ObjectUtils.isEmpty(person)) {
            FailedToAddStuVO failedToAddStuVO = new FailedToAddStuVO(data.getPersonId(), data.getMajorId(), data.getMajorName(), "未注册.");
            failedData.add(failedToAddStuVO);
            log.info("未查询到该学习者信息.已跳过注册该学生.");
            return;
            //  学生所选的专业不存在不添加
        } else if (!cachedMajorMap.containsKey(data.getMajorId())) {
            FailedToAddStuVO failedToAddStuVO = new FailedToAddStuVO(data.getPersonId(), data.getMajorId(), data.getMajorName(), "不存在该专业及类型");
            failedData.add(failedToAddStuVO);
            log.info("不存在该专业及类型.已跳过注册该学生.");
            return;
        }

        Student student = new Student(person.getName(), studentService.generateStuId(major.getMajorId()), person.getPersonId(), major.getType(), (short) 0);

        cachedDataList.add(student);
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
        saveData();
        log.info("合法的所有数据解析并保存完成！");
        // 返回注册失败的学生信息
        if (!failedData.isEmpty()) {
            throw new FailedToImportException(failedData);
        }
    }

    /**
     * 存储数据到数据库
     */
    private void saveData() {
        log.info("当前一共解析了 {} 条数据，开始存储数据库！", cachedDataList.size());
        studentService.insertStudentBatch(cachedDataList, nodeId, cachedMajorMap);
        log.info("存储数据库成功！");
    }
}
