package com.communication.netty.server;

import com.communication.netty.client.ClientChannelInboundHandlerAdapter;
import com.communication.netty.client.ClientChannelOutboundHandlerAdapter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    private final int DEFAULT_PORT = 8001;

    private int port = DEFAULT_PORT;

    private EventLoopGroup master = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();


    public NettyServer() {
    }

    public NettyServer(int port) {
        this.port = port;
    }

    public void init(){
        try{

            ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(master,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //输入


                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,1,0,1));
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new ServerChannelInboundHandlerAdapter());
                        //输出
                       // socketChannel.pipeline().addLast(new ServerChannelOutboundHandlerAdapter());
                    }
                });
            log.info("绑定端口[{}]",port);
            serverBootstrap.bind(port).sync();

        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
    }
}
