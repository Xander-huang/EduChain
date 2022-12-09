package top.zy68.fbChainService.common;

/**
 * @ClassName ReceiptStatus
 * @Description 交易回执状态码
 * 详细请参考：https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/api.html#id3
 * @Author Sustart
 * @Date2022/4/6 11:24
 * @Version 1.0
 **/
public class ReceiptStatus {
    /**
     * 交易正常
     */
    public static final String NORMAL = "0x0";
    /**
     * 交易已经在交易池中
     */
    public static final String IN_TRANSACTION_POOL = "0x2710";
}
