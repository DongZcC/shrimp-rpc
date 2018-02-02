package Proxy;

import service.HelloService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-02-02<br>
 */
public class ConsumerProxy {

    public static <T> T consumer(Class<T> clazz, String address, int port) {
        T t = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, (Object proxy, Method method, Object[] args) -> {
            // 创建client socket 连接
            Socket socket = new Socket(address, port);
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeUTF(method.getName());
                outputStream.writeObject(args);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    try {
                        Object result = inputStream.readObject();
                        if (result instanceof Throwable) {
                            throw (Throwable) result;
                        }
                        return result;
                    } finally {
                        inputStream.close();
                    }
                } finally {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                socket.close();
            }
        });
        return t;
    }
}
