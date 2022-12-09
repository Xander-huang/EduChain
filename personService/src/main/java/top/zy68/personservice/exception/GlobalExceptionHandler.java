package top.zy68.personservice.exception;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.zy68.personservice.api.ResponseBean;
import top.zy68.personservice.api.ResultCode;

import javax.validation.ConstraintViolationException;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 全局异常处理
 * @Author Sustart
 * @Date2022/1/16 17:28
 * @Version 1.0
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 方法参数校验异常处理 - @requestBody的方式
     *
     * @param e 方法参数异常
     * @return ResponseBean
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseBean handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        //获取第一个参数校验失败的失败原因
        ObjectError objectError = bindingResult.getFieldErrors().get(0);
        return new ResponseBean(ResultCode.BAD_REQUEST, objectError.getDefaultMessage());
    }

    /**
     * 参数校验异常处理 - @Validated 单个方法参数校验的方式
     *
     * @param e ConstraintViolationException
     * @return ResponseBean
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseBean handleConstraintViolationException(ConstraintViolationException e) {
        // e.getMessage() = 格式为 "方法名.变量名: XXXX"，只取最后部分返回.
        String[] msg = e.getMessage().split(": ");
        return new ResponseBean(ResultCode.BAD_REQUEST, msg[msg.length - 1]);
    }

    /**
     * Shiro校验用户认证失败
     *
     * @param e AuthenticationException
     * @return ResponseBean
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseBean handleConstraintViolationException(AuthenticationException e) {
        return new ResponseBean(ResultCode.BAD_REQUEST, "用户名或密码错误.");
    }

    /**
     * 客户端请求的业务异常处理，异常原因：来自客户端的参数错误
     *
     * @param e ClientBusinessException
     * @return ResponseBean
     */
    @ExceptionHandler(ClientBusinessException.class)
    public ResponseBean handleClientBusinessException(ClientBusinessException e) {
        return new ResponseBean(ResultCode.BAD_REQUEST, e.getMessage());
    }

    /**
     * 正常的业务处理，异常原因：某些数据不存在。目的用于反馈提示信息，业务执行正常。
     *
     * @param e NormalBusinessException
     * @return ResponseBean
     */
    @ExceptionHandler(NormalBusinessException.class)
    public ResponseBean handleNormalBusinessException(NormalBusinessException e) {
        return new ResponseBean(ResultCode.SUCCESS, e.getMessage());
    }

    /**
     * 系统内部的可捕获的异常，异常原因：系统内部业务异常。
     *
     * @param e InternalBusinessException
     * @return ResponseBean
     */
    @ExceptionHandler(InternalBusinessException.class)
    public ResponseBean handleNormalBusinessException(InternalBusinessException e) {
        return new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 未登录认证异常，需要重新登录。
     *
     * @param e InternalBusinessException
     * @return ResponseBean
     */
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseBean handleNormalBusinessException(UnAuthorizedException e) {
        return new ResponseBean(ResultCode.UNAUTHORIZED, e.getMessage());
    }

    /**
     * 捕捉其他所有异常
     *
     * @param ex 异常
     * @return 响应体
     */
    @ExceptionHandler(Exception.class)
    public ResponseBean globalException(Throwable ex) {
        return new ResponseBean(ResultCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
