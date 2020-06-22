package com.communication.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;

@Slf4j
public class InboundOutboundHandlerX implements ChannelInboundHandler , ChannelOutboundHandler {

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.fireChannelRegistered();

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.fireChannelUnregistered();

    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        log.info(channelHandlerContext.name()+ "-InboundOutboundHandlerX channelRead");
        channelHandlerContext.fireChannelRead(o);


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        channelHandlerContext.fireUserEventTriggered(o);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        channelHandlerContext.fireExceptionCaught(throwable);
    }

    @Override
    public void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {
        channelHandlerContext.bind(socketAddress,channelPromise);
    }

    @Override
    public void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress1, ChannelPromise channelPromise) throws Exception {
        channelHandlerContext.connect(socketAddress,socketAddress1);
    }

    @Override
    public void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        channelHandlerContext.disconnect();
    }

    @Override
    public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        channelHandlerContext.close();
    }

    @Override
    public void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        channelHandlerContext.deregister();
    }

    @Override
    public void read(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.read();
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
        log.info(channelHandlerContext.name()+ "-InboundOutboundHandlerX write");
        channelHandlerContext.fireChannelRead(o);
    }

    @Override
    public void flush(ChannelHandlerContext channelHandlerContext) throws Exception {
        channelHandlerContext.flush();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }
}
