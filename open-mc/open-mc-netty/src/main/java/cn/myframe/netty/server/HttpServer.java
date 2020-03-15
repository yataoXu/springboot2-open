package cn.myframe.netty.server;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.myframe.netty.pipeline.HttpPipeline;
import cn.myframe.properties.NettyHttpProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * HTTP服务
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月25日 下午5:39:36
 */
@Configuration
@EnableConfigurationProperties({NettyHttpProperties.class})
@ConditionalOnProperty(  //配置文件属性是否为true
		value = {"netty.http.enabled"},
		matchIfMissing = false
)
@Slf4j
public class HttpServer {
	
	    @Autowired
	    HttpPipeline httpPipeline;

	    @Autowired
	    NettyHttpProperties nettyHttpProperties;

	    @Bean("starHttpServer")
	    public String start() {
	        // 准备配置
	        // HttpConfiguration.me().setContextPath(contextPath).setWebDir(webDir).config();
	        // 启动服务器
	       Thread thread =  new Thread(() -> {
	            NioEventLoopGroup bossGroup =
						new NioEventLoopGroup(nettyHttpProperties.getBossThreads());
	            NioEventLoopGroup workerGroup =
						new NioEventLoopGroup(nettyHttpProperties.getWorkThreads());
	            try {
	            	log.info("start netty [HTTP] server ,port: " + nettyHttpProperties.getPort());
	                ServerBootstrap boot = new ServerBootstrap();
	                options(boot).group(bossGroup, workerGroup)
	                        .channel(NioServerSocketChannel.class)
	                        .handler(new LoggingHandler(LogLevel.INFO))
	                        .childHandler(httpPipeline);
	                Channel ch = null;
	              //是否绑定IP
	                if(StringUtils.isNotEmpty(nettyHttpProperties.getBindIp())){
	                	ch = boot.bind(nettyHttpProperties.getBindIp(),
								nettyHttpProperties.getPort()).sync().channel();
	                }else{
	                	ch = boot.bind(nettyHttpProperties.getPort()).sync().channel();
	                }
	                ch.closeFuture().sync();
	            } catch (InterruptedException e) {
	                log.error("启动NettyServer错误", e);
	            } finally {
	                bossGroup.shutdownGracefully();
	                workerGroup.shutdownGracefully();
	            }
	        });
	        thread.setName("http_Server");
	    //    thread.setDaemon(true);
	        thread.start();

	        return "http start";
	    }

	    private ServerBootstrap options(ServerBootstrap boot) {
	        /*if (HttpConfiguration.me().getSoBacklog() > 0) {
	            boot.option(ChannelOption.SO_BACKLOG, HttpConfiguration.me().getSoBacklog());
	        }*/
	        return boot;
	    }

	

}
