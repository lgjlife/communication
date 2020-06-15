package com.communication.io.bio.client;

public class SocketClientDemo {

    public static void main(String args[]){

        String host = "127.0.0.1";
        int port = 8000;

        FxSocketClient client = new FxSocketClient(host,port,"client-1");
        client.connect();

        new Thread(){
            @Override
            public void run() {
                while (true){
                    try{

                        Thread.sleep(1000);
                    }
                    catch(Exception ex){

                    }
                }
            }
        }.start();

    }
}
