package com.communication.io.file;

import lombok.extern.slf4j.Slf4j;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

@Slf4j
public class FileChannelMap {

    private String path = "/home/lgj/aaa/test.test";
    private String mode = "rw";
    private FileChannel fileChannel;
    private MappedByteBuffer mappedByteBuffer;

    public static void main(String args[]){

        FileChannelMap fileChannelMap = new FileChannelMap();
        try{
            fileChannelMap.write("adfa");
            fileChannelMap.read(1);

        }
        catch(Exception ex){

            log.error(ex.toString());
            log.info(Arrays.toString(ex.getStackTrace()));
            ex.printStackTrace();
        }
    }

    public FileChannelMap() {
        init();
    }

    public FileChannelMap(String path, String mode) {
        this.path = path;
        this.mode = mode;
        init();
    }

    private void init(){
        try{
            this.fileChannel = new RandomAccessFile(this.path,mode).getChannel();
            mappedByteBuffer =  fileChannel.map(FileChannel.MapMode.READ_WRITE,0,10);


        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
    }

    public void read(int index) throws Exception{
       // byte data =   mappedByteBuffer.get(index);
       // log.info("data = " + data);

        byte[] readByte = new byte[10];
        mappedByteBuffer.position(0);
        char c = mappedByteBuffer.getChar();
        log.info("c = " + c);

        c = mappedByteBuffer.getChar();
        log.info("c = " + c);


        c = mappedByteBuffer.getChar();
        log.info("c = " + c);

        c = mappedByteBuffer.getChar();
        log.info("c = " + c);

        c = mappedByteBuffer.getChar();
        log.info("c = " + c);

        c = mappedByteBuffer.getChar();
        log.info("c = " + c);

       // log.info("读取到的数据：" + new String(readByte,"UTF-8"));
    }

    public void write(String data){

        mappedByteBuffer.position(0);
        mappedByteBuffer.putChar('1');
        mappedByteBuffer.putChar('2');
        mappedByteBuffer.putChar('3');
        mappedByteBuffer.putChar('4');
        mappedByteBuffer.putChar('5');
      //  mappedByteBuffer.putChar('5');
    }
}
