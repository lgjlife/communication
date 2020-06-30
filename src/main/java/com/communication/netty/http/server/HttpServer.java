package com.communication.netty.http.server;

import com.communication.netty.http.server.handler.HttpRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;


@Slf4j
public class HttpServer {

    private String  host = "127.0.0.1";
    private int port = 8445;




    public void startServer(){

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup());
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {

                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(Short.MAX_VALUE));
                pipeline.addLast(new HttpRequestHandler());
            }
        });

       try{

           log.info("绑定{}:{}....",host,port);
           bootstrap.bind(new InetSocketAddress(host,port)).sync();
           log.info("绑定{}:{}成功",host,port);

       }
       catch(Exception ex){
           log.error(ex.getMessage());
       }

    }
}
