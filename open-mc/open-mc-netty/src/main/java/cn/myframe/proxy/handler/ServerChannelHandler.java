package cn.myframe.proxy.handler;

import java.util.List;

import cn.myframe.properties.NettyProxyServerProperties;
import cn.myframe.proxy.config.ProxyConfig;
import cn.myframe.proxy.protocol.Constants;
import cn.myframe.proxy.protocol.ProxyMessage;
import cn.myframe.proxy.server.ProxyChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(  //配置文件属性是否为true
        value = {"netty.proxy-server.enabled"},
        matchIfMissing = false
)
@Slf4j
@ChannelHandler.Sharable //@Sharable 注解用来说明ChannelHandler是否可以在多个channel直接共享使用
public class ServerChannelHandler   extends SimpleChannelInboundHandler<ProxyMessage>{

    @Autowired
    NettyProxyServerProperties nettyProxyServerProperties;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage proxyMessage) throws Exception {
		switch (proxyMessage.getType()) {
	        case ProxyMessage.TYPE_HEARTBEAT:
	            handleHeartbeatMessage(ctx, proxyMessage);
	            break;
	        case ProxyMessage.C_TYPE_AUTH:
	            handleAuthMessage(ctx, proxyMessage);
	            break;
	        case ProxyMessage.TYPE_CONNECT:
	            handleConnectMessage(ctx, proxyMessage);
	            break;
	        case ProxyMessage.TYPE_DISCONNECT:
	            handleDisconnectMessage(ctx, proxyMessage);
	            break;
	        case ProxyMessage.P_TYPE_TRANSFER:
	            handleTransferMessage(ctx, proxyMessage);
	            break;
	        default:
	            break;
	    }
	}
	
	/**
	 * 心跳处理
	 * @param ctx
	 * @param proxyMessage
	 */
	private void handleHeartbeatMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        ProxyMessage heartbeatMessage = new ProxyMessage();
        heartbeatMessage.setSerialNumber(heartbeatMessage.getSerialNumber());
        heartbeatMessage.setType(ProxyMessage.TYPE_HEARTBEAT);
        log.debug("response heartbeat message {}", ctx.channel());
        ctx.channel().writeAndFlush(heartbeatMessage);
    }
	
	/**
	 * 认证消息，检测clientKey是否正确
	 * @param ctx
	 * @param proxyMessage
	 */
	private void handleAuthMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String clientKey = proxyMessage.getUri();
       // List<Integer> ports = ProxyConfig.getInstance().getClientInetPorts(clientKey);
        List<Integer> ports =  nettyProxyServerProperties.getClientInetPortMapping().get(clientKey);
        if (ports == null) {
        	log.info("error clientKey {}, {}", clientKey, ctx.channel());
            ctx.channel().close();
            return;
        }

        Channel channel = ProxyChannelManager.getCmdChannel(clientKey);
        if (channel != null) {
        	log.warn("exist channel for key {}, {}", clientKey, channel);
            ctx.channel().close();
            return;
        }

        log.info("set port => channel, {}, {}, {}", clientKey, ports, ctx.channel());
        ProxyChannelManager.addCmdChannel(ports, clientKey, ctx.channel());
    }
	
	/**
	 * 代理后端服务器建立连接消息
	 * @param ctx
	 * @param proxyMessage
	 */
	private void handleConnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String uri = proxyMessage.getUri();
        if (uri == null) {
            ctx.channel().close();
            log.warn("ConnectMessage:null uri");
            return;
        }

        String[] tokens = uri.split("@");
        if (tokens.length != 2) {
            ctx.channel().close();
            log.warn("ConnectMessage:error uri");
            return;
        }

        Channel cmdChannel = ProxyChannelManager.getCmdChannel(tokens[1]);
        if (cmdChannel == null) {
            ctx.channel().close();
            log.warn("ConnectMessage:error cmd channel key {}", tokens[1]);
            return;
        }

        Channel userChannel = ProxyChannelManager.getUserChannel(cmdChannel, tokens[0]);
        if (userChannel != null) {
            ctx.channel().attr(Constants.USER_ID).set(tokens[0]);
            ctx.channel().attr(Constants.CLIENT_KEY).set(tokens[1]);
            ctx.channel().attr(Constants.NEXT_CHANNEL).set(userChannel);
            userChannel.attr(Constants.NEXT_CHANNEL).set(ctx.channel());
            // 代理客户端与后端服务器连接成功，修改用户连接为可读状态
            userChannel.config().setOption(ChannelOption.AUTO_READ, true);
        }
    }
	
	/**
	 * 代理数据传输
	 * @param ctx
	 * @param proxyMessage
	 */
	private void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel userChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
        if (userChannel != null) {
            ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
            buf.writeBytes(proxyMessage.getData());
            userChannel.writeAndFlush(buf);
        }
    }

	/**
	 * 代理后端服务器断开连接消息
	 * @param ctx
	 * @param proxyMessage
	 */
    private void handleDisconnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        String clientKey = ctx.channel().attr(Constants.CLIENT_KEY).get();

        // 代理连接没有连上服务器由控制连接发送用户端断开连接消息
        if (clientKey == null) {
            String userId = proxyMessage.getUri();
            Channel userChannel = ProxyChannelManager.removeUserChannelFromCmdChannel(ctx.channel(), userId);
            if (userChannel != null) {
                // 数据发送完成后再关闭连接，解决http1.0数据传输问题
                userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
            return;
        }

        Channel cmdChannel = ProxyChannelManager.getCmdChannel(clientKey);
        if (cmdChannel == null) {
            log.warn("ConnectMessage:error cmd channel key {}", ctx.channel().attr(Constants.CLIENT_KEY).get());
            return;
        }

        Channel userChannel = ProxyChannelManager.removeUserChannelFromCmdChannel(cmdChannel, ctx.channel().attr(Constants.USER_ID).get());
        if (userChannel != null) {
            // 数据发送完成后再关闭连接，解决http1.0数据传输问题
            userChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            ctx.channel().attr(Constants.NEXT_CHANNEL).remove();
            ctx.channel().attr(Constants.CLIENT_KEY).remove();
            ctx.channel().attr(Constants.USER_ID).remove();
        }
    }

	

}
