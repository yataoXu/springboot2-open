package cn.myframe.netty.pipeline;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.myframe.netty.handle.HttpServerHandler;
import cn.myframe.properties.NettyHttpsProperties;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.ApplicationProtocolConfig.Protocol;
import io.netty.handler.ssl.ApplicationProtocolConfig.SelectedListenerFailureBehavior;
import io.netty.handler.ssl.ApplicationProtocolConfig.SelectorFailureBehavior;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(  //配置文件属性是否为true
		value = {"netty.https.enabled"},
		matchIfMissing = false
)
@Slf4j
public class HttpsPipeline extends ChannelInitializer<SocketChannel> {

	@Autowired
	HttpServerHandler httpServerHandler;
	
	private SslContext sslContect;
	
	@Autowired
    NettyHttpsProperties nettyHttpsProperties;
	
	private SslContext getSslContext(){
		if(sslContect == null ){
			sslContect = createSslCtx(nettyHttpsProperties.getCertFile(), nettyHttpsProperties.getKeyFile());
		}
		return sslContect;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
		SSLEngine engine =  getSslContext().newEngine(ch.alloc());
        engine.setUseClientMode(false);
        engine.setNeedClientAuth(false); //不需要验证客户端
        p.addFirst("ssl",new SslHandler(engine));
		p.addLast(new HttpServerCodec());
		p.addLast(new HttpContentCompressor());
		p.addLast(new HttpObjectAggregator(1048576));
		p.addLast(new ChunkedWriteHandler());
		// http请求根处理器
		p.addLast(httpServerHandler);
	}
	
	public SslContext createSslCtx(String certificateFile, String keyFile){       
        // SslProvider provider = OpenSsl.isAlpnSupported() ? SslProvider.OPENSSL : SslProvider.JDK;
         try
         {
             //去掉GCM算法  避免对端是jdk8 ssl。
             List<String> ciphers = Arrays.asList
            		 (
                     "ECDHE-RSA-AES128-SHA",
                  /*   "ECDHE-RSA-AES128-SHA256", */
                     "ECDHE-RSA-AES256-SHA", 
                 /*    "ECDHE-RSA-AES256-SHA384", */
                     "ECDHE-ECDSA-AES128-SHA",
                  /*   "ECDHE-ECDSA-AES128-SHA256",*/
                     "ECDHE-ECDSA-AES256-SHA",
                   /*  "ECDH-RSA-AES128-GCM-SHA256",
                     "ECDH-RSA-AES256-GCM-SHA384",
                     "ECDHE-ECDSA-AES128-GCM-SHA256",
                     "ECDHE-ECDSA-AES256-GCM-SHA384",*/
                     "AES128-SHA",
                     "AES256-SHA"/*, 
                     "DES-CBC3-SHA"*/);

             SslContext sslCtx = SslContextBuilder.forServer(new File(certificateFile), new File(keyFile))
                     .sslProvider(SslProvider.OPENSSL)
                     .applicationProtocolConfig(new ApplicationProtocolConfig(
                         Protocol.NPN_AND_ALPN,
                         // NO_ADVERTISE is currently the only mode supported by both OpenSsl and JDK providers.
                         SelectorFailureBehavior.NO_ADVERTISE,
                         // ACCEPT is currently the only mode supported by both OpenSsl and JDK providers.
                         SelectedListenerFailureBehavior.ACCEPT,
                         ApplicationProtocolNames.HTTP_1_1))
                     .ciphers(ciphers)
                     .build();
           return sslCtx;
         }
         catch (SSLException e){
             log.error("Error while create ssl context. Exception follows.",e);
         }
         return null;
     }
}
