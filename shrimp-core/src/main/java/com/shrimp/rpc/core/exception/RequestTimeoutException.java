package com.shrimp.rpc.core.exception;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
public class RequestTimeoutException extends RuntimeException {
    public RequestTimeoutException(String s) {
        super(s);
    }

    public RequestTimeoutException(Throwable cause) {
        super(cause);
    }

}
