package com.shrimp.rpc.core.serializer;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-10<br>
 */
public interface Serializer {
    byte[] serializer(Object obj);

    <T> T deserializer(byte[] bytes);
}
