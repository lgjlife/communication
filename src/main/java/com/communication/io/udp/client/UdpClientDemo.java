package com.communication.io.udp.client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UdpClientDemo {

    public static void main(String args[]){


        try{

            new Thread(){
                @Override
                public void run() {
                    UdpClient udpClient = new UdpClient();
                    while (true){

                        try{
                            Thread.sleep(5000);
                            udpClient.write();
                            udpClient.read();

                        }
                        catch(Exception ex){

                            ex.printStackTrace();

                        }
                    }
                }
            }.start();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
