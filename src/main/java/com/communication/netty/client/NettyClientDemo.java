package com.communication.netty.client;


import com.communication.netty.client.data.ByteCounter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClientDemo {

    private final static  String HOST = "127.0.0.1";
    private final static  int PORT = 8001;

    public static void main(String args[]){


        NettyClientDemo nettyClientDemo = new NettyClientDemo();

        NettyClient nettyClient = new NettyClient();
        nettyClient.connect(HOST,PORT);


        nettyClientDemo.trafficSendTest(nettyClient);


    }

    public void normalSend(NettyClient nettyClient){
        try{

            Thread.sleep(1000);
            log.info("+++++++++++++++++++++");
            for(int  i= 0;i < 100000; i++){

                String sendDate = "12345";
                nettyClient.send(HOST,PORT,sendDate.getBytes());

                // Thread.sleep(1000);
                // log.info("---------------");

            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    ScheduledExecutorService excutorService  = Executors.newScheduledThreadPool(1);

    public void trafficSendTest( NettyClient nettyClient){



        excutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                try{
                    log.info("发送数据");
                    for(int i = 0; i< 10; i++){
                        nettyClient.send(HOST,PORT,new byte[1000*1000]);
                    }

                    log.info("客户端发送数据速率:{}  bytes/s", ByteCounter.counter.getAndSet(0));
                    log.info("客户端总共发送数据:{}  bytes", ByteCounter.Allcounter.get());
                }
                catch(Exception ex){
                    log.error(ex.getMessage());
                }
            }
        },0,1000, TimeUnit.MILLISECONDS);

    }
}
