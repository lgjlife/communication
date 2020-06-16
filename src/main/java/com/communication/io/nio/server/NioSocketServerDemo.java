package com.communication.io.nio.server;

import com.communication.io.bio.server.FxSocketServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NioSocketServerDemo {

    public static void main(String args[]){

        FxNIOServer socketServer = new FxNIOServer(8000);
        try{
            socketServer.startServer();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
