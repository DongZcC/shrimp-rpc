package com.shrimp.rpc.factory;

import com.shrimp.rpc.core.bootstrap.ServerBuilder;
import com.shrimp.rpc.core.server.Server;
import com.shrimp.rpc.core.server.ServerImpl;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
@Data
public class ServerFactoryBean implements FactoryBean<Object> {

    private Class<?> serviceInterface;
    private Object serviceImpl;
    private String ip;
    private int port;
    private String serviceName;
    private String zkConn;
    private ServerImpl rpcServer;


    //服务注册并提供
    public void start() {
        Server rpcServer = ServerBuilder
                .builder()
                .serviceImpl(serviceImpl)
                .serviceName(serviceName)
                .zkConn(zkConn)
                .port(port)
                .bulid();
        rpcServer.start();
    }

    //服务下线
    public void serviceOffline() {
        rpcServer.shutdown();
    }

    @Override
    public Object getObject() throws Exception {
        return this;
    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
