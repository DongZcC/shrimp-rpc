package com.shrimp.rpc.core.server;

import com.shrimp.rpc.core.protocol.RpcDecoder;
import com.shrimp.rpc.core.protocol.RpcEncoder;
import com.shrimp.rpc.core.utils.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.shrimp.rpc.core.utils.Constant.*;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-10<br>
 */
public class ServerImpl implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerImpl.class);

    private String localIp;
    private int port;
    private boolean start = false;
    private Channel channel;
    private Object serviceImpl;
    private String serviceName;
    private String zkConn;
    private String serviceRegisterPath;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private CuratorFramework curatorFramework;

    public ServerImpl(int port, Object serviceImpl, String serviceName) {
        this.port = port;
        this.serviceImpl = serviceImpl;
        this.serviceName = serviceName;
    }

    public ServerImpl(int port, Object serviceImpl, String serviceName, String zkConn) {
        this.port = port;
        this.serviceName = serviceName;
        this.serviceImpl = serviceImpl;
        this.zkConn = zkConn;
    }

    @Override
    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LoggingHandler(LogLevel.INFO))
                                .addLast(new RpcDecoder(10 * 1024 * 1024))
                                .addLast(new RpcEncoder())
                                .addLast(new RpcServerHandler(serviceImpl));
                    }
                });
        try {
            //调用bind等待客户端连接
            ChannelFuture future = serverBootstrap.bind(port).sync();

            //接着注册服务
            registerService();
            LOGGER.info("Server Started At {}", port);
            start = true;
            this.channel = future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void registerService() {
        zkConn = getZkConn();
        localIp = NetUtils.getLocalIp();
        String serviceIp = localIp + ":" + port;
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkConn)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();

        //连接上zk然后开始注册服务节点
        String serviceBasePath = ZK_DATA_PATH + serviceName;
        //添加基础服务节点
        try {
            curatorFramework.create()
                    .creatingParentContainersIfNeeded()
                    .forPath(serviceBasePath);
        } catch (Exception e) {
            if (e.getMessage().contains("NodeExist")) {
                LOGGER.info("This Path Service has already Exist");
            } else {
                LOGGER.error("Create Path Error", e);
                throw new RuntimeException("Register error");
            }
        }

        boolean registerSuccess = false;

        serviceRegisterPath = serviceBasePath + "/" + serviceIp;
        //如果添加成功，添加标识服务具体路径的节点
        while (!registerSuccess) {
            try {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(serviceRegisterPath);
                registerSuccess = true;
            } catch (Exception e) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                LOGGER.info("Retry Register ZK, {}", e.getMessage());

                try {
                    curatorFramework.delete().forPath(serviceBasePath + "/" + serviceIp);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void shutdown() {
        //关停服务相关逻辑
        LOGGER.info("Shutting down server {}", serviceName);
        unRegister();
        if (curatorFramework != null)
            curatorFramework.close();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    private void unRegister() {
        LOGGER.info("unRegister zookeeper");
        try {
            curatorFramework.delete().forPath(ZK_DATA_PATH + serviceName + "/" + localIp + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getZkConn() {
        return zkConn;
    }

    public void setZkConn(String zkConn) {
        this.zkConn = zkConn;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
