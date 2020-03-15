package cn.myframe.proxy.server;

import cn.myframe.proxy.config.ProxyConfig;
import cn.myframe.proxy.handler.UserChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.myframe.properties.NettyProxyServerProperties;
import cn.myframe.proxy.handler.ServerChannelHandler;
import cn.myframe.proxy.protocol.IdleCheckHandler;
import cn.myframe.proxy.protocol.ProxyMessageDecoder;
import cn.myframe.proxy.protocol.ProxyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.BindException;
import java.util.List;

@Configuration
@EnableConfigurationProperties({NettyProxyServerProperties.class})
@ConditionalOnProperty(  //配置文件属性是否为true
		value = {"netty.proxy-server.enabled"},
		matchIfMissing = false
)
@Slf4j
public class ProxyServer {
	
	@Autowired
    NettyProxyServerProperties nettyProxyServerProperties;

    @Autowired
    UserChannelHandler userChannelHandler;

    @Autowired
    ServerChannelHandler serverChannelHandler;
	
	/**
     * max packet is 2M.
     */
    private static final int MAX_FRAME_LENGTH = 2 * 1024 * 1024;

    private static final int LENGTH_FIELD_OFFSET = 0;

    private static final int LENGTH_FIELD_LENGTH = 4;

    private static final int INITIAL_BYTES_TO_STRIP = 0;

    private static final int LENGTH_ADJUSTMENT = 0;
	
	@Bean("startProxyServer")
	public String start(){
    //    new ProxyConfig( nettyProxyServerProperties);
        Thread thread =  new Thread(() -> {
            ServerBootstrap bootstrap = new ServerBootstrap();
            NioEventLoopGroup serverBossGroup = new NioEventLoopGroup();
            NioEventLoopGroup serverWorkerGroup = new NioEventLoopGroup();
            bootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProxyMessageDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                    ch.pipeline().addLast(new ProxyMessageEncoder());
                    ch.pipeline().addLast(new IdleCheckHandler(IdleCheckHandler.READ_IDLE_TIME, IdleCheckHandler.WRITE_IDLE_TIME, 0));
                    ch.pipeline().addLast(serverChannelHandler);
                }
            });
            try {
                log.info("start netty [Proxy Server] server ,port:" + nettyProxyServerProperties.getPort());
                bootstrap.bind( nettyProxyServerProperties.getPort()).get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            startUserPort();
        });
        thread.setName("Proxy_Server");
        thread.start();
		return "";
	}


    private void startUserPort() {
        Thread thread =  new Thread(() -> {
            ServerBootstrap bootstrap = new ServerBootstrap();
            NioEventLoopGroup serverBossGroup = new NioEventLoopGroup();
            NioEventLoopGroup serverWorkerGroup = new NioEventLoopGroup();
            bootstrap.group(serverBossGroup, serverWorkerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    //  ch.pipeline().addFirst(new BytesMetricsHandler());
                    ch.pipeline().addLast(userChannelHandler);
                }
            });
            //  List<Integer> ports = ProxyConfig.getInstance().getUserPorts();
            List<Integer> ports = nettyProxyServerProperties.ports();
            for (int port : ports) {
                try {
                    bootstrap.bind(port).get();
                    log.info("bind user port " + port);
                } catch (Exception ex) {
                    // BindException表示该端口已经绑定过
                    if (!(ex.getCause() instanceof BindException)) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        thread.setName("UseSever_Server");
        thread.start();

    }
	
	

}
