package com.communication.netty.client.handler;

import com.communication.netty.client.data.ByteCounter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;

@Slf4j
public class ClientChannelTrafficShapingHandler extends ChannelOutboundHandlerAdapter {



    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        EventLoop eventLoop =  ctx.channel().eventLoop();
        int len = ((ByteBuf)msg).readableBytes();
        log.info("发送数据的长度为:"+len);

        ByteCounter.counter.getAndAdd(len);
        ByteCounter.Allcounter.getAndAdd(len);
        super.write(ctx, msg, promise);
    }
}
