package top.zy68.allianceservice.exception;

/**
 * @ClassName NormalBusinessException
 * @Description 正常的业务异常，用于反馈提示信息。
 * @Author Sustart
 * @Date2022/3/8 13:10
 * @Version 1.0
 **/
public class NormalBusinessException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public NormalBusinessException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NormalBusinessException(String message) {
        super(message);
    }
}
