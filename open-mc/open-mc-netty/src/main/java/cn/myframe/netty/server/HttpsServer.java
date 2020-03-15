package cn.myframe.netty.server;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.myframe.netty.pipeline.HttpsPipeline;
import cn.myframe.properties.NettyHttpProperties;
import cn.myframe.properties.NettyHttpsProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * HTTPS服务
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月25日 下午5:39:36
 */
@Configuration
@EnableConfigurationProperties({NettyHttpsProperties.class})
@ConditionalOnProperty(  //配置文件属性是否为true
		value = {"netty.https.enabled"},
		matchIfMissing = false
)
@Slf4j
public class HttpsServer {
	
	    @Autowired
	    HttpsPipeline httpsPipeline;

	    @Autowired
	    NettyHttpsProperties nettyHttpsProperties;

	    @Bean("starHttpsServer")
	    public String start() {
	        // 准备配置
	        // HttpConfiguration.me().setContextPath(contextPath).setWebDir(webDir).config();
	        // 启动服务器
	       Thread thread =  new Thread(() -> {
	    	    NioEventLoopGroup bossGroup = new NioEventLoopGroup(nettyHttpsProperties.getBossThreads());
		        NioEventLoopGroup workerGroup = new NioEventLoopGroup(nettyHttpsProperties.getWorkThreads());
	            try {
	            	log.info("start netty [HTTPS] server ,port: " + nettyHttpsProperties.getPort());
	                ServerBootstrap boot = new ServerBootstrap();
	                options(boot).group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.ERROR))
	                        .childHandler(httpsPipeline);
	                Channel ch = null;
	                //是否绑定IP
	                if(StringUtils.isNotEmpty(nettyHttpsProperties.getBindIp())){
	                	ch = boot.bind(nettyHttpsProperties.getBindIp(),nettyHttpsProperties.getPort()).sync().channel();
	                }else{
	                	ch = boot.bind(nettyHttpsProperties.getPort()).sync().channel();
	                }
	                ch.closeFuture().sync();
	            } catch (InterruptedException e) {
	                log.error("启动NettyServer错误", e);
	            } finally {
	                bossGroup.shutdownGracefully();
	                workerGroup.shutdownGracefully();
	            }
	        });
	        thread.setName("https_Server");
	        thread.start();

	        return "https start";
	    }

	    private ServerBootstrap options(ServerBootstrap boot) {
	        /*if (HttpConfiguration.me().getSoBacklog() > 0) {
	            boot.option(ChannelOption.SO_BACKLOG, HttpConfiguration.me().getSoBacklog());
	        }*/
	        return boot;
	    }

	

}
