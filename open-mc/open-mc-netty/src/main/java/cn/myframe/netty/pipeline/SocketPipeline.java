package cn.myframe.netty.pipeline;

import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import cn.myframe.netty.handle.SocketServerHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author ynz
 * @version 创建时间：2018/6/26
 * @email ynz@myframe.cn
 */
@Component
@ConditionalOnProperty(  //配置文件属性是否为true
        value = {"netty.socket.enabled"},
        matchIfMissing = false
)
public class SocketPipeline extends ChannelInitializer<SocketChannel>{
	
	@Autowired
	SocketServerHandler socketServerHandler;
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		/*IpSubnetFilterRule[] ipsf = new IpSubnetFilterRule[1];
		ipsf[0] = new IpSubnetFilterRule("10.10.1.1",16,IpFilterRuleType.REJECT);
		pipeline.addLast(new RuleBasedIpFilter(ipsf));*/
		pipeline.addLast(new StringEncoder());
		pipeline.addLast(new StringDecoder());
		pipeline.addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\r\n\r\n".getBytes())));
		pipeline.addLast(socketServerHandler);

		
	}

}
