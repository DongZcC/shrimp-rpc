package com.shrimp.rpc.core.rpcproxy;

import com.shrimp.rpc.core.client.Client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-16<br>
 */
public class DynamicRpcProxy implements RpcProxy {

    @Override
    public <T> T proxyInterface(Client client, Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] {serviceInterface}, new DynamicInvocationHandler(client, serviceInterface));
    }

    private static class DynamicInvocationHandler implements InvocationHandler {

        private Client client;
        private Class<?> serviceInterface;


        public DynamicInvocationHandler(Client client, Class<?> serviceInterface) {
            this.client = client;
            this.serviceInterface = serviceInterface;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return client.sendMessage(serviceInterface, method, args).getResponse();
        }
    }
}
