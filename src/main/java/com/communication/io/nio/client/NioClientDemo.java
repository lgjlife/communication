package com.communication.io.nio.client;

import com.communication.io.common.ClientDataHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class NioClientDemo {

    public static void main(String args[]) throws Exception{

        String host = "127.0.0.1";
        int port = 8000;


        for(int i = 0; i< 2; i++){
            NioClient client = new NioClient(host,port,"client-1");
            client.connect();
            try{
                client.request("客户端数据:"+new Random().nextInt(100),new ClientDataHandler(){
                    @Override
                    public void handler(Object object) {
                        log.info("收到服务器的数据:"+object);
                    }
                });
            }
            catch(Exception ex){
                ex.printStackTrace();
            }


        }



        while (true){
            try{

                Thread.sleep(1000);
            }
            catch(Exception ex){

            }
        }

    }
}
