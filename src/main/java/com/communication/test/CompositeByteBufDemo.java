package com.communication.test;

import io.netty.buffer.*;
import lombok.extern.slf4j.Slf4j;

import javax.rmi.CORBA.Util;

@Slf4j
public class CompositeByteBufDemo {

    public static void main(String args[]){

        ByteBufAllocator allocator = new UnpooledByteBufAllocator(true);

        CompositeByteBuf byteBufs = allocator.compositeBuffer();

        ByteBuf byteBuf1 = allocator.buffer();
        ByteBuf byteBuf2 = allocator.buffer();

        byteBuf1.writeBytes("123456".getBytes());
        byteBuf2.writeBytes("7890".getBytes());

        byteBufs.addComponent(true,byteBuf1);



        System.out.println("添加byteBuf1之后,byteBufs = " + byteBufs);
        byteBufs.addComponent(true,byteBuf2);
        System.out.println("添加byteBuf2之后,byteBufs = " + byteBufs);

        int len = byteBufs.readableBytes();
        System.out.println("len = " + len);
        byte[] result = new byte[len];
        byteBufs.readBytes(result);

        System.out.println("byteBufs str = " + new String(result));

        ByteBuf component0 =  byteBufs.component(0);
        len = component0.readableBytes();
        System.out.println("len = " + len);
        result = new byte[len];
        component0.readBytes(result);
        System.out.println("component0 str = " + new String(result));


        ByteBuf component1 =  byteBufs.component(1);
        len = component1.readableBytes();
        System.out.println("len = " + len);
        result = new byte[len];
        component1.readBytes(result);

        System.out.println("component1 str = " + new String(result));





    }
}
