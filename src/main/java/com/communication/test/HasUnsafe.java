package com.communication.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

//-Dio.netty.noUnsafe=true
//-Dio.netty.noUnsafe=false -Dio.netty.allocator.numDirectArenas=0
@Slf4j
public class HasUnsafe {

    public static void main(String args[]){

        try{

            Thread.sleep(1000);
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }

        System.out.println();


        boolean flag =   PlatformDependent.hasUnsafe();

        log.info("Unsafe = " + flag);


        ByteBufAllocator pooledByteBufAllocator = new PooledByteBufAllocator(false);
        log.info("pooledByteBufAllocator.isDirectBufferPooled = " + pooledByteBufAllocator.isDirectBufferPooled() );

        ByteBuf poolBuf = pooledByteBufAllocator.ioBuffer();
        log.info("poolBuf isDirect = " + poolBuf.isDirect());

        ByteBufAllocator unpooledByteBufAllocator = new UnpooledByteBufAllocator(false);
        log.info("unpooledByteBufAllocator.isDirectBufferPooled = " + unpooledByteBufAllocator.isDirectBufferPooled() );
        ByteBuf uppoolBuf = unpooledByteBufAllocator.ioBuffer();
        log.info("uppoolBuf isDirect = " + uppoolBuf.isDirect());


    }
}
