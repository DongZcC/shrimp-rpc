package com.shrimp.rpc.core.bootstrap;

import com.google.common.base.Preconditions;
import com.shrimp.rpc.core.client.ClientImpl;
import com.shrimp.rpc.core.rpcproxy.CglibRpcProxy;
import com.shrimp.rpc.core.rpcproxy.RpcProxy;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
public class ClientBuilder<T> {

    private String serviceName;
    private String zkConn;
    private Class<T> serviceInterface;
    private int requestTimeoutMillis = 10000;
    private Class<? extends RpcProxy> clientProxyClass = CglibRpcProxy.class;

    public static <T> ClientBuilder<T> builder() {
        return new ClientBuilder<>();
    }

    public ClientBuilder<T> serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public ClientBuilder<T> zkConn(String zkConn) {
        this.zkConn = zkConn;
        return this;
    }

    public ClientBuilder<T> serviceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
        return this;
    }

    public ClientBuilder<T> requestTimeout(int requestTimeoutMillis) {
        this.requestTimeoutMillis = requestTimeoutMillis;
        return this;
    }

    public ClientBuilder<T> clientProxyClass(Class<? extends RpcProxy> clientProxyClass) {
        this.clientProxyClass = clientProxyClass;
        return this;
    }

    public T build() {
        //因Curator底层依赖guava，刚好可以拿来验证
        Preconditions.checkNotNull(serviceInterface);
        Preconditions.checkNotNull(zkConn);
        Preconditions.checkNotNull(serviceName);
        ClientImpl client = new ClientImpl(this.serviceName);
        client.setZkConn(this.zkConn);
        client.setRequestTimeoutMillis(this.requestTimeoutMillis);
        client.init();
        return client.proxyInterface(this.serviceInterface);
    }
}
