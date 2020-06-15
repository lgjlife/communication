package com.communication.io.bio.server;

public class SocketServerDemo {

    public static void main(String args[]){

        FxSocketServer socketServer = new FxSocketServer(8000,10);
        socketServer.acceptTask();
    }
}
