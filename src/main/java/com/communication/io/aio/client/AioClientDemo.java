package com.communication.io.aio.client;

import com.communication.io.bio.common.ClientDataHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class AioClientDemo {

    public static void main(String args[]){
        for(int i = 0; i< 2 ; i++){
            FxAioClient client = new FxAioClient("127.0.0.1",8000);
            client.connect();


            try{
                Thread.sleep(1000);
                client.request("客户端数据:"+new Random().nextInt(100),new ClientDataHandler(){
                    @Override
                    public void handler(Object object) {
                        log.info("收到服务器的数据:"+object);
                    }
                });

                Thread.sleep(100);
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
