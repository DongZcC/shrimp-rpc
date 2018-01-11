package com.shrimp.rpc.example.service;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
public class HelloWorldImpl implements HelloWorld {
    @Override
    public String say(String hello) {
        return "server :" + hello;
    }

    @Override
    public int sum(int a, int b) {
        return a + b;
    }

    @Override
    public int max(Integer a, Integer b) {
        return a <= b ? b : a;
    }
}
