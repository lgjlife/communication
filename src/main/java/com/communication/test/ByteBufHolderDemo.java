package com.communication.test;

import io.netty.buffer.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteBufHolderDemo {

    public static void main(String args[]){
        ByteBufAllocator allocator = new UnpooledByteBufAllocator(true);
        ByteBuf buf = allocator.buffer(20);
        buf.writeBytes("123456".getBytes());
        ByteBufHolder holder = new DefaultByteBufHolder(buf);

        log.info("buf = " + buf.readBytes(1));


        ByteBufHolder holder1 = holder.duplicate();
        ByteBuf buf1 = holder1.content();
        log.info("buf1 = " + buf1.readBytes(1));


        log.info("buf = " + buf.readBytes(1));
        log.info("buf1 = " + buf1.readBytes(1));
    }
}
