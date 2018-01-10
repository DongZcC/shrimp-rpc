package com.shrimp.rpc.core.protocol;

import com.shrimp.rpc.core.serializer.KryoSerializer;
import com.shrimp.rpc.core.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-10<br>
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {

    private Serializer serializer = new KryoSerializer();

    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byte[] bytes = serializer.serializer(o);
        int length = bytes.length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(bytes);
    }
}
