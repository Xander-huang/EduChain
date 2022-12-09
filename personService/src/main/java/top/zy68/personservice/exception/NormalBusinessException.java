package top.zy68.personservice.exception;

/**
 * @ClassName NormalBusinessException
 * @Description 请求正常的业务异常，用于反馈提示信息。
 * @Author Sustart
 * @Date2022/3/8 13:10
 * @Version 1.0
 **/
public class NormalBusinessException extends RuntimeException {
    /**
     * 请求正常的业务异常，用于反馈提示信息。
     */
    public NormalBusinessException() {
        super();
    }

    /**
     * 请求正常的业务异常，用于反馈提示信息。
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NormalBusinessException(String message) {
        super(message);
    }
}
