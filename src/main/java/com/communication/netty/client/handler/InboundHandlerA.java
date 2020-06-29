package com.communication.netty.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public class InboundHandlerA extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info(ctx.name()+"- InboundHandlerA - channelRead,len = " + ((ByteBuf)msg).readableBytes() );

        ByteBuf msg1 = (ByteBuf)msg;
        byte[] read = new byte[msg1.readableBytes()];

        msg1.readBytes(read);


        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();

        super.exceptionCaught(ctx, cause);
    }
}
