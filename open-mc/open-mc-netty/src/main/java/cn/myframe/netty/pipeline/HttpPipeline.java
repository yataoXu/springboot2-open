package cn.myframe.netty.pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import cn.myframe.netty.handle.HttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

@Component
@ConditionalOnProperty(  //配置文件属性是否为true
		value = {"netty.http.enabled"},
		matchIfMissing = false
)
public class HttpPipeline extends ChannelInitializer<SocketChannel> {

	@Autowired
	HttpServerHandler httpServerHandler;


	@Override
	public void initChannel(SocketChannel ch) {
	//	log.error("test", this);
		ChannelPipeline p = ch.pipeline();		
		p.addLast(new HttpServerCodec());
		p.addLast(new HttpContentCompressor());
		// HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
		p.addLast(new HttpObjectAggregator(1048576));
		//ChunkedWriteHandler：向客户端发送HTML5文件
		p.addLast(new ChunkedWriteHandler());
		// http请求根处理器
		p.addLast(httpServerHandler);
	}
	
}
