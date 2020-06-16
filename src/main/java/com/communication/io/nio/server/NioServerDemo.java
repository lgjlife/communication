package com.communication.io.nio.server;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NioServerDemo {

    public static void main(String args[]){

        NioServer socketServer = new NioServer(8000);
        try{
            socketServer.startServer();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
