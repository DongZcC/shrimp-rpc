package com.shrimp.rpc.core.protocol;

import lombok.Data;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-10<br>
 */
@Data
public class Request {
    private long requestId;
    private Class<?> clazz;
    private String method;
    private Object[] params;
    private Class<?>[] parameterTypes;
    private long requestTime;
}
