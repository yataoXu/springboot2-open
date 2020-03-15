package cn.myframe.netty.server;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.myframe.netty.pipeline.SocketPipeline;
import cn.myframe.properties.NettySocketProperties;
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
 * @author ynz
 * @version 创建时间：2018/6/26
 * @email ynz@myframe.cn
 */
@Configuration
@EnableConfigurationProperties({NettySocketProperties.class})
@ConditionalOnProperty(  //配置文件属性是否为true
		value = {"netty.socket.enabled"},
		matchIfMissing = false
)
@Slf4j
public class SocketServer {
	
	@Autowired
    SocketPipeline socketPipeline;

    @Autowired
    NettySocketProperties nettySocketProperties;
    
    @Bean("starSocketServer")
    public String start() {
        Thread thread =  new Thread(() -> {
        	NioEventLoopGroup bossGroup = new NioEventLoopGroup(nettySocketProperties.getBossThreads());
	        NioEventLoopGroup workerGroup = new NioEventLoopGroup(nettySocketProperties.getWorkThreads());
            try {
                log.info("start netty [SocketServer] server ,port: " + nettySocketProperties.getPort());
                ServerBootstrap boot = new ServerBootstrap();
                options(boot).group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(socketPipeline);
                Channel ch = null;
               //是否绑定IP
                if(StringUtils.isNotEmpty(nettySocketProperties.getBindIp())){
                	ch = boot.bind(nettySocketProperties.getBindIp(),nettySocketProperties.getPort()).sync().channel();
                }else{
                	ch = boot.bind(nettySocketProperties.getPort()).sync().channel();
                }
                ch.closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("启动NettyServer错误", e);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
        thread.setName("Socket_Server");
        thread.start();
        return "ws start";
    }


    private ServerBootstrap options(ServerBootstrap boot) {
 /*       boot.option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);*/
    	
    	boot.option(ChannelOption.SO_BACKLOG, 1024)
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
			.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
        return boot;
    }

}
