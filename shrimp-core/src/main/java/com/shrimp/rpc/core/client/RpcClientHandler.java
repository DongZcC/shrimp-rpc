package com.shrimp.rpc.core.client;

import com.shrimp.rpc.core.protocol.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

import static com.shrimp.rpc.core.utils.ResponseMapHelper.responseMap;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
@ChannelHandler.Sharable   // 在多个不同的channel中共享使用 responseMap的blockingQueue，所以要增加此注释
public class RpcClientHandler extends SimpleChannelInboundHandler<Response> {

    private final static Logger LOGGER = LoggerFactory.getLogger(RpcClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        BlockingQueue<Response> blockingQueue = responseMap.get(msg.getRequestId());
        if (blockingQueue != null) {
            blockingQueue.put(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Exception caught on {}", ctx.channel(), cause);
        ctx.channel().close();
    }
}
