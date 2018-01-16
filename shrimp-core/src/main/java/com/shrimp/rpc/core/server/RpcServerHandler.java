package com.shrimp.rpc.core.server;

import com.shrimp.rpc.core.protocol.Request;
import com.shrimp.rpc.core.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-10<br>
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<Request> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private Object service;

    public RpcServerHandler(Object service) {
        this.service = service;
    }

    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
        Class clazz = msg.getClazz();
        String methodName = msg.getMethod();
        Object[] params = msg.getParams();
        Class<?>[] parameterTypes = msg.getParameterTypes();
        long requestId = msg.getRequestId();
        //通过反射来获取客户端所要调用的方法
        Method method = service.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        Object invoke = method.invoke(service, params);
        Response response = new Response();
        response.setRequestId(requestId);
        response.setResponse(invoke);
        ctx.pipeline().writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Exception caught on {},", ctx.channel(), cause);
        ctx.channel().close();
    }
}
