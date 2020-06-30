package com.communication.netty.http.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HttpResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse fullHttpResponse) throws Exception {

        HttpHeaders headers =  fullHttpResponse.headers();
        log.info("fullHttpResponse = " + fullHttpResponse);


        //ctx.fireChannelRead(fullHttpResponse);
    }
}
