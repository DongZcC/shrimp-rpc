package com.shrimp.rpc.core.rpcproxy;

import com.shrimp.rpc.core.client.Client;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
public interface RpcProxy {
    <T> T proxyInterface(Client client, final Class<T> serviceInterface);
}
