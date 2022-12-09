package top.zy68.personservice.exception;

/**
 * @ClassName UnAuthorizedException
 * @Description 自定义的未授权异常
 * @Author Sustart
 * @Date2022/1/16 18:52
 * @Version 1.0
 **/
public class UnAuthorizedException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public UnAuthorizedException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UnAuthorizedException(String message) {
        super(message);
    }
}