package com.communication.io.file;

import lombok.extern.slf4j.Slf4j;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class FileChannelDemo {

    public static void main(String args[]){

        try{
            NioFileChannel nioFileChannel = new NioFileChannel("/home/lgj/aaa/test.test","rw");
            nioFileChannel.write("飞翔的开发了和");
            Thread.sleep(100);
            nioFileChannel.read(0);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }



    }
}
