package com.shrimp.rpc.example.server;

import com.shrimp.rpc.example.service.HelloWorld;
import com.shrimp.rpc.example.service.HelloWorldImpl;
import com.shrimp.rpc.factory.ServerFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
@SpringBootApplication
public class SpringServerConfig {

    @Bean
    public HelloWorld hello() {
        return new HelloWorldImpl();
    }

    @Bean
    public ServerFactoryBean serverFactoryBean() {
        final ServerFactoryBean serverFactoryBean = new ServerFactoryBean();
        serverFactoryBean.setPort(9090);
        serverFactoryBean.setServiceInterface(HelloWorld.class);
        //自出自定义的名字就相当于注解
        serverFactoryBean.setServiceName("hello");
        serverFactoryBean.setServiceImpl(hello());
        serverFactoryBean.setZkConn("127.0.0.1:2181");

        new Thread(()->{
            try{
                serverFactoryBean.start();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }, "RpcServer").start();
        return serverFactoryBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringServerConfig.class, "--server.port=8082");
    }
}
