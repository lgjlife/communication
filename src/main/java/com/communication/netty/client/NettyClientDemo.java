package com.communication.netty.client;


import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class NettyClientDemo {
    public static void main(String args[]){

        NettyClient nettyClient = new NettyClient();
        nettyClient.init();


        try{


            Thread.sleep(1000);
            log.info("+++++++++++++++++++++");
            for(int  i= 0;i < 1; i++){

                String sendDate = "12345";
                nettyClient.send("127.0.0.1",8001,sendDate.getBytes());

               // Thread.sleep(1000);
               // log.info("---------------");

            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }



    }
}
