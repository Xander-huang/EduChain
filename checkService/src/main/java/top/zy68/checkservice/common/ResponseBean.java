package top.zy68.checkservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName ResponseBean
 * @Description return data to the frontend
 * @Author Sustart
 * @Date2021/12/21 15:20
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean implements Serializable {
    /**
     * http状态码
     */
    private Integer code;
    /**
     * 状态描述信息
     */
    private String message;
    /**
     * 数据
     */
    private Object data;

    /**
     * 封装默认成功的含状态码的数据返回体
     *
     * @param data 返回体数据
     */
    public ResponseBean(Object data) {
        this(ResultCode.SUCCESS, data);
    }

    /**
     * 封装含状态码的数据返回体
     *
     * @param resultCode 状态码及描述对象
     * @param data       返回体数据
     */
    public ResponseBean(ResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public ResponseBean(ResultCode resultCode, Object data,String message) {
        this.code = resultCode.getCode();
        this.message =message;
        this.data = data;
    }
}
