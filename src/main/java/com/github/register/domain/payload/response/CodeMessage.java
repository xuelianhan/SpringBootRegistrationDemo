package com.github.register.domain.payload.response;

/**
 * @author sniper
 * @date 17 Sep 2023
 */
public class CodeMessage {
    /**
     * Signs of Success
     */
    public static final Integer CODE_SUCCESS = 0;
    /**
     * Default failure flag, other failure meanings can be customized
     */
    public static final Integer CODE_DEFAULT_FAILURE = -1;

    private Integer code;
    private String message;
    private Object data;

    public CodeMessage() {}

    public CodeMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
