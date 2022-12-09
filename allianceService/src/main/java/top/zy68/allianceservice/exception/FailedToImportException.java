package top.zy68.allianceservice.exception;

/**
 * @ClassName FailedToImportException
 * @Description Excel导入数据中，部分数据导入失败的异常，返回导入失败数据
 * @Author Sustart
 * @Date2022/3/29 14:41
 * @Version 1.0
 **/
public class FailedToImportException extends RuntimeException {
    /**
     * 导入失败的数据
     */
    private final Object data;

    /**
     * 构造方法，传入导入失败的数据对象
     *
     * @param data 失败数据
     */
    public FailedToImportException(Object data) {
        this.data = data;
    }

    /**
     * 获取保存的数据
     *
     * @return 数据对象
     */
    public Object getData() {
        return this.data;
    }
}
