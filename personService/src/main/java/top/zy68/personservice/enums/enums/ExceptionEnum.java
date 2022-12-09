package top.zy68.personservice.enums.enums;

/**
 * @Author: wangxingyu
 * @Description:
 * @Date: 10:15 2020/9/29
 */
public enum  ExceptionEnum {

    UNKNOWN_EXCEPTION(50000,"未知错误");

    private final Integer code;
    private final String msg;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
