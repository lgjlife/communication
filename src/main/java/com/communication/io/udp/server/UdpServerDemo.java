package com.communication.io.udp.server;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UdpServerDemo {

    public static void main(String args[]){
        UdpServer udpServer = new UdpServer();
        try{

            udpServer.startServer();
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
    }
}
