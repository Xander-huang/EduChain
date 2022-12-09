package top.zy68.personservice.exception;


import top.zy68.personservice.enums.enums.ExceptionEnum;

/**
 * @Author: wangxingyu
 * @Description:
 * @Date: 10:03 2020/9/29
 */
public class AuthException extends RuntimeException{

    public AuthException() {
        super();
    }

    public AuthException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
