package cn.myframe.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ExecutionException;

/**
 * @Author: ynz
 * @Date: 2019/2/20/020 10:08
 * @Version 1.0
 */
public class SocketClient {

    public static void main(String[] args) {
        start(1,1);
    }



    public static void start(final int beginPort, int nPort) {
        System.out.println("client starting....");
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
            }
        });
        int port = 4445;
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", port);
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                System.out.println("connect failed, exit!");
                //  System.exit(0);
            }
        });
        try {
            channelFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
