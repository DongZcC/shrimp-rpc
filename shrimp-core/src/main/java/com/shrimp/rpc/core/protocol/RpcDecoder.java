package com.shrimp.rpc.core.protocol;

import com.shrimp.rpc.core.serializer.KryoSerializer;
import com.shrimp.rpc.core.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能说明: <br>
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-10<br>
 */
public class RpcDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcDecoder.class);
    private Serializer serializer = new KryoSerializer();

    public RpcDecoder(int maxFrameLength) {
        super(maxFrameLength, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode != null) {
            int byteLength = decode.readableBytes();
            byte[] byteHolder = new byte[byteLength];
            decode.readBytes(byteHolder);
            Object deserialize = serializer.deserializer(byteHolder);
            return deserialize;
        }
        LOGGER.debug("Decoder Result is null");
        return null;
    }
}
