package com.communication.io.bio.server;

public class BioSocketServerDemo {

    public static void main(String args[]){

        BioSocketServer socketServer = new BioSocketServer(8000,10);
        socketServer.acceptTask();
    }
}
