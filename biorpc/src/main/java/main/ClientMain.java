package main;

import Proxy.ConsumerProxy;
import service.HelloService;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-02-02<br>
 */
public class ClientMain {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            HelloService service = ConsumerProxy.consumer(HelloService.class, "localhost", 8083);
            String hello = service.sayHello("dzc" + i);
            System.out.println(hello);
            Thread.sleep(2000);
        }
    }
}
