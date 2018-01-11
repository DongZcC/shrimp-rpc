package com.shrimp.rpc.core.client;

import com.shrimp.rpc.core.protocol.Response;

import java.lang.reflect.Method;

/**
 * 功能说明: 服务的发现与使用<br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
public interface Client {
    Response sendMessage(Class<?> clazz, Method method, Object[] args);
    <T> T proxyInterface(Class<T> serviceInterface);
    void close();
}
