package com.shrimp.rpc.factory;

import com.shrimp.rpc.core.bootstrap.ClientBuilder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
@Data
public class ClientFactoryBean<T> implements FactoryBean<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientFactoryBean.class);
    private Class<T> serviceInterface;
    private String serviceName;
    private String zkConn;

    @Override
    public T getObject() throws Exception {
        return ClientBuilder
                .<T>builder()
                .zkConn(zkConn)
                .serviceName(serviceName)
                .serviceInterface(serviceInterface)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
