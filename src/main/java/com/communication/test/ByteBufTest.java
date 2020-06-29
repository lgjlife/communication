package com.communication.test;

import com.communication.jvm.JvmUtil;
import com.communication.netty.util.DirectMemReport;
import io.netty.buffer.*;
import lombok.extern.slf4j.Slf4j;

//-Dio.netty.leakDetectionLevel=advanced  DISABLED SIMPLE ADVANCED PARANOID
/*
关闭：这种模式下不进行泄露监控。
简单：这种模式下以1/128的概率抽取ByteBuf进行泄露监控。
增强：在简单的基础上，每一次对ByteBuf的调用都会尝试记录调用轨迹，消耗较大。
偏执：在增强的基础上，对每一个ByteBuf都进行泄露监控，消耗最大。
* */
//　-Xms=1000m -Xmx=1000m -Xmn=800
//-XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m
@Slf4j
public class ByteBufTest {

    public static void main(String args[]){

        JvmUtil.printMem();

        System.out.println(Runtime.getRuntime().maxMemory()/1024/1024);

       // headBuf();retain();
        new DirectMemReport();
        ByteBufAllocator poolByteBufAllocator = new PooledByteBufAllocator(true);
        ByteBufAllocator unpoolByteBufAllocator = new UnpooledByteBufAllocator(true);
        poolAndUnpoolTest(poolByteBufAllocator);



    }

    public void creat(){

        Unpooled.buffer();


    }

    public static  void retain(){

        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        ByteBuf buf = allocator.buffer(100);
        log.info("buf refCnt = " + buf.refCnt());

        buf.retain();
        log.info("buf refCnt = " + buf.refCnt());

        log.info("buf = " + buf);
        buf.release();
        buf.release();
        buf.writeByte(12);
        log.info("buf = " + buf);


    }


    public static void headBuf(){

        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        ByteBuf buf = allocator.buffer(100);

        log.info("buf = " + buf);




        int count = 0;
        byte[] data = new byte[1024*1024];
        for(int i = 0; i< data.length; i++){
            data[i] = 12;
        }

//        while(false){
//
//            buf.writeBytes(data);
//
//            log.info("buf = " + buf);
//            log.info("已经分配了-{}M",++count);
//
//            try{
//
//                Thread.sleep(20);
//            }
//            catch(Exception ex){
//                ex.printStackTrace();
//            }
//            System.out.println();
//        }

    }
    public static void poolAndUnpoolTest(ByteBufAllocator allocator ){


        int times = Integer.MAX_VALUE;

        int _1K = 1024;
        int _1M = 1024*1024;

        log.info("开始执行任务");
        long startTime = System.currentTimeMillis();

        for(int i = 0; i< times; i++){

            ByteBuf buf = allocator.buffer(_1M*500);

          //  byte[] data = new byte[_1M*500];
            //buf.writeBytes(data);



            //log.info("allocator = " + allocator);
            buf.release();
            try{

                Thread.sleep(1);
            }
            catch(Exception ex){
                log.error(ex.getMessage());
            }
        }
        long endTime = System.currentTimeMillis();

        log.info("执行[{}次总共花费时间[{}]ms",times,endTime-startTime);
    }

}
