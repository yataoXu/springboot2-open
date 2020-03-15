package cn.myframe.netty.server;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.myframe.netty.pipeline.WsPipeline;
import cn.myframe.properties.NettyWsProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * WebSocket服务
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月26日 下午4:09:48
 */
@Configuration
@EnableConfigurationProperties({NettyWsProperties.class})
@ConditionalOnProperty(  //配置文件属性是否为true
		value = {"netty.ws.enabled"},
		matchIfMissing = false
)
@Slf4j
public class WsServer {
	
	    @Autowired
	    WsPipeline wsPipeline;

	    @Autowired
	    NettyWsProperties nettyWsProperties;
	    
	    @Bean("starWsServer")
	    public String start() {
	        // 准备配置
	        // HttpConfiguration.me().setContextPath(contextPath).setWebDir(webDir).config();
	        // 启动服务器
	       Thread thread =  new Thread(() -> {
	    	    NioEventLoopGroup bossGroup = new NioEventLoopGroup(nettyWsProperties.getBossThreads());
		        NioEventLoopGroup workerGroup = new NioEventLoopGroup(nettyWsProperties.getWorkThreads());
	            try {
	            	log.info("start netty [WebSocket] server ,port: " + nettyWsProperties.getPort());
	                ServerBootstrap boot = new ServerBootstrap();
	                options(boot).group(bossGroup, workerGroup)
	                        .channel(NioServerSocketChannel.class)
	                        .handler(new LoggingHandler(LogLevel.INFO))
	                        .childHandler(wsPipeline);
	                Channel ch = null;
	                //是否绑定IP
	                if(StringUtils.isNotEmpty(nettyWsProperties.getBindIp())){
	                	ch = boot.bind(nettyWsProperties.getBindIp(),nettyWsProperties.getPort()).sync().channel();
	                }else{
	                	ch = boot.bind(nettyWsProperties.getPort()).sync().channel();
	                }
	                ch.closeFuture().sync();
	            } catch (InterruptedException e) {
	                log.error("启动NettyServer错误", e);
	            } finally {
	                bossGroup.shutdownGracefully();
	                workerGroup.shutdownGracefully();
	            }
	        });
	        thread.setName("Ws_Server");
	        thread.start();
	        return "ws start";
	    }
	    
	    
	    private ServerBootstrap options(ServerBootstrap boot) {
	    	boot.option(ChannelOption.SO_BACKLOG, 1024)
			    .option(ChannelOption.TCP_NODELAY, true)
			    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
	        return boot;
	    }

}
