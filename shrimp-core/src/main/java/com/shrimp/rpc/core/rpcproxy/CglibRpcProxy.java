package com.shrimp.rpc.core.rpcproxy;

import com.shrimp.rpc.core.client.Client;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
public class CglibRpcProxy implements RpcProxy {
    @Override
    public <T> T proxyInterface(Client client, Class<T> serviceInterface) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(serviceInterface);
        enhancer.setCallback(new CglibInterceptor(client, serviceInterface));
        Object enhancedObject = enhancer.create();
        return (T)enhancedObject;
    }

    /**
     * 静态内部类，来做Method的cglib代理
     */
    private static class CglibInterceptor implements MethodInterceptor {

        //首先判断所要代理的方式是否为通用方法，是的话就返回此代理对象的相关内容
        private static Method hashCodeMethod;
        private static Method equalsMethod;
        private static Method toStringMethod;

        static {
            try {
                hashCodeMethod = Object.class.getMethod("hashCode");
                equalsMethod = Object.class.getMethod("equals", Object.class);
                toStringMethod = Object.class.getMethod("toString");
            } catch (NoSuchMethodException e) {
                throw new NoSuchMethodError(e.getMessage());
            }
        }


        /*
           针对几个方法做相应的策略
         */

        private int proxyHashCode(Object proxy) {
            return System.identityHashCode(proxy);
        }

        private boolean proxyEquals(Object proxy, Object other) {
            return (proxy == other);
        }

        private String proxyToString(Object proxy) {
            return proxy.getClass().getName() + '@' + Integer.toHexString(proxy.hashCode());
        }


        private Client client;

        private Class<?> serviceInterface;

        public CglibInterceptor(Client client, Class<?> serviceInterface) {
            this.client = client;
            this.serviceInterface = serviceInterface;
        }


        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            if (hashCodeMethod.equals(method)) {
                return proxyHashCode(methodProxy);
            }
            if (equalsMethod.equals(method)) {
                return proxyEquals(methodProxy, objects[0]);
            }
            if (toStringMethod.equals(method)) {
                return proxyToString(methodProxy);
            }
            return client.sendMessage(serviceInterface, method, objects).getResponse();
        }
    }
}
