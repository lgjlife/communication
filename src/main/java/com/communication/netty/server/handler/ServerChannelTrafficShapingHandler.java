package com.communication.netty.server.handler;

import com.communication.netty.client.data.ByteCounter;
import com.communication.netty.server.data.ServerByteCounter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ServerChannelTrafficShapingHandler extends ChannelInboundHandlerAdapter {

    ScheduledExecutorService excutorService  = Executors.newScheduledThreadPool(1);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        log.info("与客户端 {} 建立连接",ctx.channel().remoteAddress());
        excutorService  = Executors.newScheduledThreadPool(1);
        ServerByteCounter.Allcounter.set(0);
        excutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                int counter = ServerByteCounter.counter.getAndSet(0);
                if(counter!= 0){
                    log.info("－－－－－服务端接收数据速率:{}  bytes/s", counter);
                }

               // log.info("服务端总共接收数据:{}  bytes", ServerByteCounter.Allcounter.get());

            }
        },0,1000, TimeUnit.MILLISECONDS);


        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        log.info("与客户端 {} 断开连接",ctx.channel().remoteAddress());
        excutorService.shutdownNow();
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        int len = ((ByteBuf)msg).readableBytes();
       // log.info("接收数据的长度为:"+len);

        ServerByteCounter.counter.getAndAdd(len);
        ServerByteCounter.Allcounter.getAndAdd(len);
        super.channelRead(ctx, msg);
    }



}
