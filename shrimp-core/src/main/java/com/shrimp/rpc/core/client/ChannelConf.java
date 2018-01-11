package com.shrimp.rpc.core.client;

import com.shrimp.rpc.core.utils.ConnectionObjectFactory;
import io.netty.channel.Channel;
import lombok.Data;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * 功能说明: <br>
 * 然后做个channel包装:
 * 主要还是为了得到一个池GenericObjectPool:
 * 系统版本: v1.0<br>
 * 开发人员: @author dongzc15247<br>
 * 开发时间: 2018-01-11<br>
 */
@Data
public class ChannelConf {
    private String connStr;
    private String host;
    private int port;
    private Channel channel;
    private ObjectPool<Channel> channelObjectPool;

    public ChannelConf(String host, int port) {
        this.host = host;
        this.port = port;
        this.connStr = host + ":" + port;
        channelObjectPool = new GenericObjectPool<Channel>(new ConnectionObjectFactory(host, port));
    }

    public void close() {
        channelObjectPool.close();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChannelConf{");
        sb.append("connStr='").append(connStr).append('\'');
        sb.append(", ip='").append(host).append('\'');
        sb.append(", port=").append(port);
        sb.append(", channel=").append(channel);
        sb.append(", channelObjectPool=").append(channelObjectPool);
        sb.append('}');
        return sb.toString();
    }
}
