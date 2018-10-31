package com.haiyu;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Title: NettyClient
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/10/31 17:27
 */
public class NettyClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8081"));

    public static void main(String[] args) throws Exception {

        // 1.1 创建Reactor线程池
        EventLoopGroup group = new NioEventLoopGroup();
        try {// 1.2 创建启动类Bootstrap实例，用来设置客户端相关参数
            Bootstrap b = new Bootstrap();
            b.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);

            b.group(group)// 1.2.1设置线程池
                    .channel(NioSocketChannel.class)// 1.2.2指定用于创建客户端NIO通道的Class对象
                    .option(ChannelOption.TCP_NODELAY, true)// 1.2.3设置客户端套接字参数
                    .handler(new ChannelInitializer<SocketChannel>() {// 1.2.4设置用户自定义handler
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            p.addLast(new StringDecoder());
                            p.addLast(new NettyClientHandler());

                        }
                    });

            // 1.3启动链接
            ChannelFuture f = b.connect(HOST, PORT).sync();

            // 1.4 同步等待链接断开
            f.channel().closeFuture().sync();
        } finally {
            // 1.5优雅关闭线程池
            group.shutdownGracefully();
        }
    }
}
