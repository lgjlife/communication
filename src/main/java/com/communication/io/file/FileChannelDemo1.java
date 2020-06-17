package com.communication.io.file;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.StandardOpenOption;

@Slf4j
public class FileChannelDemo1 {

    public static void main(String args[]){

        String path = "/home/lgj/aaa/test.test";
        try{
            NioFileChannel nioFileChannel = new NioFileChannel(path,"rw");
            nioFileChannel.write("飞翔的开发了和");
            Thread.sleep(100);
            nioFileChannel.read(0);


            ///////////////////////////////



            new Thread(){

                @Override
                public void run() {
                   try{

                       AioFileChannel aioFileChannel = new AioFileChannel(path, StandardOpenOption.READ,StandardOpenOption.WRITE);

                       aioFileChannel.write("但是萨基喝咖啡大环史蒂夫放大到达境",0);
                       Thread.sleep(100);
                       aioFileChannel.read(0);
                   }
                   catch(Exception ex){
                       ex.printStackTrace();
                   }
                }
            }.start();
            AioFileChannel aioFileChannel = new AioFileChannel(path, StandardOpenOption.READ,StandardOpenOption.WRITE);

            aioFileChannel.write("但是萨基喝咖啡大环史蒂夫放大到达境",0);
            Thread.sleep(100);
            aioFileChannel.read(0);



            Thread.sleep(Integer.MAX_VALUE);

        }
        catch(Exception ex){
            ex.printStackTrace();
        }






    }
}
