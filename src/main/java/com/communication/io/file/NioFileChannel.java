package com.communication.io.file;

import lombok.extern.slf4j.Slf4j;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class NioFileChannel {

    private String fileName;
    private String mode;
    FileChannel fileChannel;

    public NioFileChannel(String fileName, String mode) {
        this.fileName = fileName;
        this.mode = mode;

        try{
            fileChannel =  new RandomAccessFile(fileName,mode).getChannel();
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }


    }

    public void write(String data) throws Exception{

        log.info("fileChannel.position = "+fileChannel.position());
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(data.getBytes());

        buf.flip();

        while(buf.hasRemaining()) {
            fileChannel.write(buf);
        }
        fileChannel.force(true);
        log.info("fileChannel.position = "+fileChannel.position());



    }

    public void read(int position)throws Exception{
        ByteBuffer readBuf = ByteBuffer.allocate(100);
        log.info("readBuf = " + readBuf);

        fileChannel.position(position);
        int readC =  fileChannel.read(readBuf);
        log.info("readBuf = " + readBuf);

        if(readC != -1){
            readBuf.flip();
            byte[] data = new byte[readC];
            readBuf.get(data);
            log.info("读取到的数据:"+new String(data,"UTF-8"));


        }
        else {
            log.info("未读取到任何数据");
        }
    }
}
