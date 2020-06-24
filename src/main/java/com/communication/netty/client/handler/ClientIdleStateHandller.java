package com.communication.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ClientIdleStateHandller extends IdleStateHandler {


    public ClientIdleStateHandller(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {

        IdleState idleState =  evt.state();
        if(idleState == IdleState.READER_IDLE){
            log.info("读空闲");

        }

        if(idleState == IdleState.WRITER_IDLE){
            log.info("写空闲");

        }


        super.channelIdle(ctx, evt);
    }
}
