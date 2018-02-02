package Proxy;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-02-02<br>
 */
public class ProviderReflect {

    private static final ExecutorService exec = Executors.newCachedThreadPool();

    public static void provider(Object service, int port) throws IOException {
        if (service == null || port < 0 || port > 65535)
            throw new IllegalArgumentException("参数非法");
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            final Socket socket = serverSocket.accept();
            exec.execute(() -> {
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    try {
                        String methodName = inputStream.readUTF();
                        Object[] args = (Object[]) inputStream.readObject();
                        Object result = MethodUtils.invokeMethod(service, methodName, args);
                        try {
                            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                            try {
                                outputStream.writeObject(result);
                            } finally {
                                outputStream.close();
                            }
                        } finally {
                            inputStream.close();
                        }
                    } finally {
                        socket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}

