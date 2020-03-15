package cn.myframe.netty.pipeline;

import cn.myframe.netty.handle.FileServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author ynz
 * @version 创建时间：2018/6/26
 * @email ynz@myframe.cn
 */
@Component
@ConditionalOnProperty(  //配置文件属性是否为true
        value = {"netty.file.enabled"},
        matchIfMissing = false
)
public class FilePipeline extends ChannelInitializer<SocketChannel> {

    @Autowired
    FileServerHandler fleServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast("http-decoder", new HttpRequestDecoder());
        p.addLast("http-aggregator", new HttpObjectAggregator(65536));
        p.addLast("http-encoder", new HttpResponseEncoder());
        p.addLast("http-chunked", new ChunkedWriteHandler());
        p.addLast("fileServerHandler",fleServerHandler);
    }
}
