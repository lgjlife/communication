package com.communication.netty.test;

import io.netty.buffer.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class ByteBufTest {

    public static void main(String args[]){

        ByteBuf byteBuf = Unpooled.buffer(100);
        for(int i = 0; i< 100; i++){
            byteBuf.writeByte(i);
        }


        int nioBufferCount = byteBuf.nioBufferCount();
        System.out.println("nioBufferCount = " + nioBufferCount);

        ByteBuffer byteBuffer =  byteBuf.nioBuffer();
        byteBuf.setByte(50,61);
        byte data = byteBuffer.get(50);

        System.out.println("ByteBuf修改index50 为61,ByteBuffer data = " + data);


        byteBuffer.put(14,(byte)99);
        byte data1 = byteBuf.getByte(14);

        System.out.println("ByteBuffer修改index14为99,　ByteBuf data1 = " + data1);







    }
}
