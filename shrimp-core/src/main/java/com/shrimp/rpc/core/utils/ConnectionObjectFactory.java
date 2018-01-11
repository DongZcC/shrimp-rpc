package com.shrimp.rpc.core.utils;

import com.shrimp.rpc.core.client.RpcClientHandler;
import com.shrimp.rpc.core.protocol.RpcDecoder;
import com.shrimp.rpc.core.protocol.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 功能说明: <br>
 * 池对象工厂(PooledObjectFactory接口):
 * 用来创建池对象, 将不用的池对象进行钝化(passivateObject),
 * 对要使用的池对象进行激活(activeObject),
 * 对池对象进行验证(validateObject),
 * 对有问题的池对象进行销毁(destroyObject)等工作
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
public class ConnectionObjectFactory extends BasePooledObjectFactory<Channel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionObjectFactory.class);

    private String ip;
    private int port;

    public ConnectionObjectFactory(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private Channel createNewConChannel() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO))
                                .addLast(new RpcDecoder(10 * 1024 * 1024))
                                .addLast(new RpcEncoder())
                                .addLast(new RpcClientHandler());
                    }
                });
        try {
            final ChannelFuture f = bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .connect(ip, port).sync();
            f.addListener(future -> {
                if (future.isSuccess()) {
                    LOGGER.info("Connect success {}", f);
                }
            });

            final Channel channel = f.channel();
            channel.closeFuture().addListener(future -> LOGGER.info("Channel Close {} {}", ip, port));
            return channel;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public Channel create() throws Exception {
        return createNewConChannel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public void destroyObject(PooledObject<Channel> p) throws Exception {
        p.getObject().close().addListener(future -> LOGGER.info("close Finish"));
    }

    @Override
    public boolean validateObject(PooledObject<Channel> p) {
        Channel object = p.getObject();
        return object.isActive();
    }
}
