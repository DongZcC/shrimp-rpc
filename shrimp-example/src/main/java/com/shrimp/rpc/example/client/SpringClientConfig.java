package com.shrimp.rpc.example.client;

import com.shrimp.rpc.example.service.HelloWorld;
import com.shrimp.rpc.factory.ClientFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
@Configuration
@RestController
@SpringBootApplication
@RequestMapping("/test")
public class SpringClientConfig {
    @Bean
    public HelloWorld clientFactoryBean() throws Exception {
        ClientFactoryBean<HelloWorld> clientFactoryBean = new ClientFactoryBean<>();
        clientFactoryBean.setZkConn("127.0.0.1:2181");
        clientFactoryBean.setServiceName("hello");
        clientFactoryBean.setServiceInterface(HelloWorld.class);
        return clientFactoryBean.getObject();
    }
    @Resource
    private HelloWorld helloWorld;
    @RequestMapping("/hello")
    public String hello(String say) {
        return helloWorld.say(say);
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringClientConfig.class, "--server.port=8081");
    }
}

