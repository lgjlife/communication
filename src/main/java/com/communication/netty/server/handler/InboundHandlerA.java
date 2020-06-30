package com.communication.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class InboundHandlerA extends ChannelInboundHandlerAdapter {

    private AtomicLong recvSice = new AtomicLong(0);

    private PooledByteBufAllocator allocator = new PooledByteBufAllocator(true);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        log.info("清空记录....");
        recvSice.set(0);
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        long allRecvSize = recvSice.addAndGet(((ByteBuf)msg).readableBytes());

        //log.info("总共接受了{}字节",allRecvSize);


        byte[] data = "12364566578534423".getBytes();
        ByteBuf message = allocator.buffer(data.length);
        message.writeBytes(data);

        log.info(ctx.name()+"- InboundHandlerA - channelRead　len={}",((ByteBuf)msg).readableBytes());

        ByteBuf msg1 = (ByteBuf)msg;
        byte[] read = new byte[msg1.readableBytes()];

        //msg1.readBytes(read);

        ctx.channel().writeAndFlush(message);
        super.channelRead(ctx, msg);


    }
}
