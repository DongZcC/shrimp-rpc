package main;

import Proxy.ProviderReflect;
import service.HelloService;
import service.HelloServiceImpl;

import java.io.IOException;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-02-02<br>
 */
public class ServerMain {
    public static void main(String[] args) throws IOException {
        HelloService service = new HelloServiceImpl();
        ProviderReflect.provider(service, 8083);
    }
}
