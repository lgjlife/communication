package com.communication.netty.server;

import lombok.extern.slf4j.Slf4j;
import com.communication.netty.util.DirectMemReport;
@Slf4j
public class NettyServerDemo {
    public static void main(String args[]){

        new DirectMemReport();

        NettyServer nettyServer = new NettyServer();
        nettyServer.init();
    }
}
