package com.communication.io.nio.client;

import com.communication.io.bio.server.FxSocketServer;

public class SocketClientDemo {

    public static void main(String args[]){

        FxSocketServer socketServer = new FxSocketServer(8000,10);
        socketServer.acceptTask();
    }
}
