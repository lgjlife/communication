package com.communication.netty.client;


import com.communication.netty.client.data.ByteCounter;
import com.communication.netty.util.DirectMemReport;
import io.netty.bootstrap.ServerBootstrap;
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

        new DirectMemReport();

       for(int i = 0; i< 1; i++){

           NettyClientDemo nettyClientDemo = new NettyClientDemo();
           NettyClient nettyClient = new NettyClient();
           nettyClient.connect(HOST,PORT);
           nettyClientDemo.trafficSendTest(nettyClient);
       }
    }

    public void trafficSendTest( NettyClient nettyClient){



        excutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                int times = 10;
                int size =1;

                try{
                    log.info("发送数据 size = {}",times*size);
                    for(int i = 0; i< times; i++){
                        nettyClient.send(HOST,PORT,new byte[size]);
                    }

                    long counter = ByteCounter.counter.getAndSet(0);

                    log.info("客户端发送数据速率:{} bytes/s,{} k/s,{} M/s", counter,counter/1024.0,counter/1024.0/1024);
                    log.info("客户端总共发送数据:{}  bytes", ByteCounter.Allcounter.get());
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        },0,1, TimeUnit.SECONDS);

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


}
