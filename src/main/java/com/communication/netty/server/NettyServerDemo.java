package com.communication.netty.server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerDemo {
    public static void main(String args[]){

        NettyServer nettyServer = new NettyServer();
        nettyServer.init();
    }
}
