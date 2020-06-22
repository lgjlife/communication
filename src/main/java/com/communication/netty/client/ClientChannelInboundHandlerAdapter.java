package com.communication.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class ClientChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {

    public ClientChannelInboundHandlerAdapter() {
        super();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        log.info("注册[{}]",ctx.channel().remoteAddress());



    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        log.info("取消注册[{}]",ctx.channel().remoteAddress());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       // super.channelActive(ctx);
        log.info("[{}]激活了...",ctx.channel().remoteAddress());
       // ctx.writeAndFlush(Unpooled.copiedBuffer("客户端发送的数据".getBytes("UTF-8")));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("取消[{}]激活了...",ctx.channel().remoteAddress());


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      //  super.channelRead(ctx, msg);
        log.info(ctx.name() + "  - ClientChannelInboundHandlerAdapter--channelRead");
        log.info("msg = " + msg);

        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
