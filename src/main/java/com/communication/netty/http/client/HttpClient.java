package com.communication.netty.http.client;

import com.communication.netty.http.client.handler.HttpResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public class HttpClient {

    private Channel channel;
    private Bootstrap bootstrap;

    public HttpClient() {

        init();
    }

    public void init(){

        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {

                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new HttpClientCodec());
                pipeline.addLast(new HttpObjectAggregator(Short.MAX_VALUE));
                pipeline.addLast(new HttpResponseHandler());
            }
        });


    }


    public void connect(String host ,int port){

        log.info("正在连接{}:{}....",host,port);
        try{

            ChannelFuture future  =  bootstrap.connect(host,port).sync();
            channel  = future.channel();
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
        log.info("连接{}:{}成功",host,port);
    }

    public void send(String data){

        ByteBuf body =  Unpooled.copiedBuffer(data, CharsetUtil.UTF_8);

        HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.GET,
                "http://localhost:8445",body);

        HttpHeaders headers = request.headers();
        headers.set("aa",123);

        request.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        channel.writeAndFlush(request);
    }
}
