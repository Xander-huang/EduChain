package top.zy68.allianceservice.api;


/**
 * @ClassName ResultCode
 * @Description http状态码定义
 * @Author Sustart
 * @Date2022/02/16 20:51
 * @Version 1.0
 **/
public enum ResultCode {


    /**
     * 请求成功
     */
    SUCCESS(200, "OK"),

    /**
     * Client请求报文中存在语法错误
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 未经授权
     */
    UNAUTHORIZED(401, "Unauthorized"),
    /**
     * 服务器找不到对应资源
     */
    SOURCE_IS_LOST(404, "Not Found"),

    /**
     * 服务器出错，无法完成请求
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");


    private final Integer code;
    private final String message;

    /**
     * 构造方法
     *
     * @param code    状态码
     * @param message 描述
     */
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * 获取状态信息
     *
     * @return 状态信息
     */
    public String getMessage() {
        return this.message;
    }
}
