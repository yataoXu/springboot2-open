package cn.myframe.proxy.handler;

import cn.myframe.proxy.client.ClientChannelMannager;
import cn.myframe.proxy.client.listener.ChannelStatusListener;
import cn.myframe.proxy.client.listener.ProxyChannelBorrowListener;
import cn.myframe.proxy.protocol.Constants;
import cn.myframe.proxy.protocol.ProxyMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * 
 * 代理客户端
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月27日 下午6:13:03
 */
@Slf4j
public class ClientChannelHandler extends SimpleChannelInboundHandler<ProxyMessage> {

    private static Logger logger = LoggerFactory.getLogger(ClientChannelHandler.class);

    private Bootstrap bootstrap;

    private Bootstrap proxyBootstrap;

    private ChannelStatusListener channelStatusListener;

    private String keys;

    public ClientChannelHandler(Bootstrap bootstrap, Bootstrap proxyBootstrap, ChannelStatusListener channelStatusListener,String keys) {
        this.bootstrap = bootstrap;
        this.proxyBootstrap = proxyBootstrap;
        this.channelStatusListener = channelStatusListener;
        this.keys = keys;
    }

    /**
     * 事件触发
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt ;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE){
                log.info("已经 10 秒没有发送信息！");
                //向服务端发送消息

            }
        }
        super.userEventTriggered(ctx, evt);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProxyMessage proxyMessage) throws Exception {
        logger.debug("recieved proxy message, type is {}", proxyMessage.getType());
        switch (proxyMessage.getType()) {
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

    private void handleTransferMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel realServerChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
        if (realServerChannel != null) {
            ByteBuf buf = ctx.alloc().buffer(proxyMessage.getData().length);
            buf.writeBytes(proxyMessage.getData());
            logger.debug("write data to real server, {}", realServerChannel);
            realServerChannel.writeAndFlush(buf);
        }
    }

    private void handleDisconnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        Channel realServerChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
        logger.debug("handleDisconnectMessage, {}", realServerChannel);
        if (realServerChannel != null) {
            ctx.channel().attr(Constants.NEXT_CHANNEL).remove();
            ClientChannelMannager.returnProxyChanel(ctx.channel());
            realServerChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void handleConnectMessage(ChannelHandlerContext ctx, ProxyMessage proxyMessage) {
        final Channel cmdChannel = ctx.channel();
        final String userId = proxyMessage.getUri();
        String[] serverInfo = new String(proxyMessage.getData()).split(":");
        String ip = serverInfo[0];
        int port = Integer.parseInt(serverInfo[1]);
        InetSocketAddress sa =  (InetSocketAddress)ctx.channel().remoteAddress();
        bootstrap.connect(ip, port).addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                // 连接后端服务器成功
                if (future.isSuccess()) {
                    final Channel realServerChannel = future.channel();
                    logger.debug("connect realserver success, {}", realServerChannel);

                    realServerChannel.config().setOption(ChannelOption.AUTO_READ, false);

                    // 获取连接
                    ClientChannelMannager.borrowProxyChanel(sa.getPort(),proxyBootstrap, new ProxyChannelBorrowListener() {

                        @Override
                        public void success(Channel channel) {
                            // 连接绑定
                            channel.attr(Constants.NEXT_CHANNEL).set(realServerChannel);
                            realServerChannel.attr(Constants.NEXT_CHANNEL).set(channel);

                            // 远程绑定
                            ProxyMessage proxyMessage = new ProxyMessage();
                            proxyMessage.setType(ProxyMessage.TYPE_CONNECT);
                            proxyMessage.setUri(userId + "@" + keys);
                            channel.writeAndFlush(proxyMessage);

                            realServerChannel.config().setOption(ChannelOption.AUTO_READ, true);
                            ClientChannelMannager.addRealServerChannel(userId, realServerChannel);
                            ClientChannelMannager.setRealServerChannelUserId(realServerChannel, userId);
                        }

                        @Override
                        public void error(Throwable cause) {
                            ProxyMessage proxyMessage = new ProxyMessage();
                            proxyMessage.setType(ProxyMessage.TYPE_DISCONNECT);
                            proxyMessage.setUri(userId);
                            cmdChannel.writeAndFlush(proxyMessage);
                        }
                    });

                } else {
                    ProxyMessage proxyMessage = new ProxyMessage();
                    proxyMessage.setType(ProxyMessage.TYPE_DISCONNECT);
                    proxyMessage.setUri(userId);
                    cmdChannel.writeAndFlush(proxyMessage);
                }
            }
        });
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel realServerChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
        if (realServerChannel != null) {
            realServerChannel.config().setOption(ChannelOption.AUTO_READ, ctx.channel().isWritable());
        }
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        // 控制连接
        if (ClientChannelMannager.getCmdChannel() == ctx.channel()) {
            ClientChannelMannager.setCmdChannel(null);
            ClientChannelMannager.clearRealServerChannels();
            channelStatusListener.channelInactive(ctx);
        } else {
            // 数据传输连接
            Channel realServerChannel = ctx.channel().attr(Constants.NEXT_CHANNEL).get();
            if (realServerChannel != null && realServerChannel.isActive()) {
                realServerChannel.close();
            }
        }

        ClientChannelMannager.removeProxyChanel(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exception caught", cause);
        super.exceptionCaught(ctx, cause);
    }

}