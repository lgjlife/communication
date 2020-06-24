package com.communication.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import lombok.extern.slf4j.Slf4j;
import sun.nio.ch.SelectorImpl;

//-XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m
@Slf4j
public class ByteBufTest {

    public static void main(String args[]){

        headBuf();


    }


    public static void headBuf(){

        PooledByteBufAllocator allocator = new PooledByteBufAllocator(true);
        ByteBuf buf = allocator.buffer(100);



        int count = 0;
        byte[] data = new byte[1024*1024];
        for(int i = 0; i< data.length; i++){
            data[i] = 12;
        }
        while(true){

            buf.writeBytes(data);

            log.info("buf = " + buf);
            log.info("已经分配了-{}M",++count);
//          //  buf.release();
//            log.info("buf = {},buf refCnt ={}",buf,buf.refCnt());
//
//            buf.retain();
//            log.info("retain buf = {},buf refCnt ={}",buf,buf.refCnt());
//
//            log.info("allocator = {}",allocator);




            try{

                Thread.sleep(20);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            System.out.println();
        }

    }
}
