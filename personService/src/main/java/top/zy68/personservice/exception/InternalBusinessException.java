package top.zy68.personservice.exception;

/**
 * @ClassName InternalBusinessException
 * @Description 系统内部业务本身的异常
 * @Author Sustart
 * @Date2022/3/11 13:10
 * @Version 1.0
 **/
public class InternalBusinessException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public InternalBusinessException() {
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
    public InternalBusinessException(String message) {
        super(message);
    }
}
