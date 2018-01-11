package com.shrimp.rpc.core.bootstrap;

import com.google.common.base.Preconditions;
import com.shrimp.rpc.core.server.Server;
import com.shrimp.rpc.core.server.ServerImpl;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
public class ServerBuilder {
    private int port;
    private String serviceName;
    private Object serviceImpl;
    private String zkConn;

    private ServerBuilder() {

    }

    public ServerBuilder port(int port) {
        this.port = port;
        return this;
    }

    public ServerBuilder serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public ServerBuilder serviceImpl(Object serviceImpl) {
        this.serviceImpl = serviceImpl;
        return this;
    }

    public ServerBuilder zkConn(String zkConn) {
        this.zkConn = zkConn;
        return this;
    }

    public Server bulid() {
        Preconditions.checkNotNull(serviceImpl);
        Preconditions.checkNotNull(serviceName);
        Preconditions.checkNotNull(zkConn);
        Preconditions.checkArgument(port > 0);
        return new ServerImpl(this.port, this.serviceImpl, this.serviceName, this.zkConn);
    }

    public static ServerBuilder builder() {
        return new ServerBuilder();
    }

}

