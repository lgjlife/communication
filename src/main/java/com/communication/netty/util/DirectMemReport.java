package com.communication.netty.util;

import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class DirectMemReport {

    private float preUse = 0;

    //netty已经使用的直接内存
    private Field DIRECT_MEMORY_COUNTER_FIELD;
    private AtomicLong cur_mem;

    //应用最大的直接内存
    private Field MAX_DIRECT_MEMORY_FIELD;
    private long max_mem;

    private static final float DIV_M = 1024*1024;
    private static final float DIV_K = 1024;
    private static final int DIV_BYTE = 1;


    public DirectMemReport() {
        init();
    }

    public void init(){
        //-Xmx2000m

        DIRECT_MEMORY_COUNTER_FIELD =  ReflectionUtils.findField(PlatformDependent.class,"DIRECT_MEMORY_COUNTER");
        DIRECT_MEMORY_COUNTER_FIELD.setAccessible(true);

        MAX_DIRECT_MEMORY_FIELD =  ReflectionUtils.findField(PlatformDependent.class,"MAX_DIRECT_MEMORY");
        MAX_DIRECT_MEMORY_FIELD.setAccessible(true);

        try{

            cur_mem = (AtomicLong)DIRECT_MEMORY_COUNTER_FIELD.get(PlatformDependent.class);
            max_mem = (long)MAX_DIRECT_MEMORY_FIELD.get(PlatformDependent.class);
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
        //定时任务，每秒打印netty使用的直接内存
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable(){
            @Override
            public void run() {
                report();
            }
        },0,1, TimeUnit.SECONDS);

    }

    public void report(){
        try{


            float curSizeM  = (cur_mem.get()/DIV_M);
            float maxSizeM  = (float)(max_mem/DIV_M);
            log.info("netty使用的直接内存:新增[{}]M,总申请[{}]M,最大容量[{}]M",(curSizeM-preUse),curSizeM,maxSizeM);

            preUse = curSizeM;

            //JvmUtil.printMem();
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
    }
}
