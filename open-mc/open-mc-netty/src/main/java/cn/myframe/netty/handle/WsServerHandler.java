package cn.myframe.netty.handle;

import cn.myframe.properties.NettyWsProperties;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable //@Sharable 注解用来说明ChannelHandler是否可以在多个channel直接共享使用
@ConditionalOnProperty(  //配置文件属性是否为true
		value = {"netty.ws.enabled"},
		matchIfMissing = false
)
public class WsServerHandler extends ChannelInboundHandlerAdapter {

	@Autowired
	NettyWsProperties nettyWsProperties;

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private WebSocketServerHandshaker handshaker;
	//websocket握手升级绑定页面
	String wsFactoryUri = "";

	@Value("${netty.ws.endPoint:/ws}")
	private String wsUri;
	//static Set<Channel> channelSet = new HashSet<>();

	/*
	 * 握手建立
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		channels.add(incoming);
	}

	/*
	 * 握手取消
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		channels.remove(incoming);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}
	//websocket消息处理（只支持文本）
	public void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {

		// 关闭请求
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		// ping请求
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 只支持文本格式，不支持二进制消息
		if (frame instanceof TextWebSocketFrame) {
			//接收到的消息
			String requestmsg = ((TextWebSocketFrame) frame).text();
			TextWebSocketFrame tws = new TextWebSocketFrame(requestmsg);
			channels.writeAndFlush(tws);
		}

	}

	// 第一次请求是http请求，请求头包括ws的信息
	public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request)
			throws Exception {
		// 如果HTTP解码失败，返回HTTP异常
		if (request instanceof HttpRequest) {
			HttpMethod method = request.getMethod();
			// 如果是websocket请求就握手升级
			if (wsUri.equalsIgnoreCase(request.getUri())) {
				System.out.println(" req instanceof HttpRequest");
				WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
						wsFactoryUri, null, false);
				handshaker = wsFactory.newHandshaker(request);
				if (handshaker == null) {
					WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
				} else {
				}
				handshaker.handshake(ctx.channel(), request);
			}
		}
	}
	
	// 异常处理，netty默认是关闭channel
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
		
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {

			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				// 读数据超时
			} else if (event.state() == IdleState.WRITER_IDLE) {
				// 写数据超时
			} else if (event.state() == IdleState.ALL_IDLE) {
				// 通道长时间没有读写，服务端主动断开链接
				ctx.close();
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}
