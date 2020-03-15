package cn.myframe.netty.server;

import cn.myframe.netty.pipeline.FilePipeline;
import cn.myframe.netty.pipeline.WsPipeline;
import cn.myframe.properties.NettyFileProperties;
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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ynz
 * @version 创建时间：2018/6/26
 * @email ynz@myframe.cn
 */
@Configuration
@EnableConfigurationProperties({NettyFileProperties.class})
@ConditionalOnProperty(  //配置文件属性是否为true
        value = {"netty.file.enabled"},
        matchIfMissing = false
)
@Slf4j
public class FileServer {
    @Autowired
    FilePipeline filePipeline;

    @Autowired
    NettyFileProperties nettyFileProperties;

    @Bean("starFileServer")
    public String start() {
        Thread thread =  new Thread(() -> {
        	NioEventLoopGroup bossGroup = new NioEventLoopGroup(nettyFileProperties.getBossThreads());
	        NioEventLoopGroup workerGroup = new NioEventLoopGroup(nettyFileProperties.getWorkThreads());
            try {
                log.info("start netty [FileServer] server ,port: " + nettyFileProperties.getPort());
                ServerBootstrap boot = new ServerBootstrap();
                options(boot).group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(filePipeline);
                Channel ch = null;
              //是否绑定IP
                if(StringUtils.isNotEmpty(nettyFileProperties.getBindIp())){
                	ch = boot.bind(nettyFileProperties.getBindIp(),nettyFileProperties.getPort()).sync().channel();
                }else{
                	ch = boot.bind(nettyFileProperties.getPort()).sync().channel();
                }
                ch.closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("启动NettyServer错误", e);
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
        thread.setName("File_Server");
        thread.start();
        return "file start";
    }


    private ServerBootstrap options(ServerBootstrap boot) {
 /*       boot.option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);*/
        return boot;
    }
}
