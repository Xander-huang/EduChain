package top.zy68.allianceservice.exception;

/**
 * @ClassName ClientBusinessException
 * @Description 业务异常，异常原因：来自客户端的参数错误
 * @Author Sustart
 * @Date2022/3/7 20:41
 * @Version 1.0
 **/
public class ClientBusinessException extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ClientBusinessException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ClientBusinessException(String message) {
        super(message);
    }
}
