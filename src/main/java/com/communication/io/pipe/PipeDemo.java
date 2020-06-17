package com.communication.io.pipe;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

@Slf4j
public class PipeDemo {

    private Pipe pipe;
    private Pipe.SinkChannel sinkChannel;
    private  Pipe.SourceChannel sourceChannel;

    public void init(){
        try{
             pipe = Pipe.open();
             sinkChannel =  pipe.sink();
             sourceChannel = pipe.source();
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }


    }

    public void write() throws Exception{
        sinkChannel.write(ByteBuffer.wrap("123456".getBytes()));
    }

    public void read() throws Exception{

        ByteBuffer buf = ByteBuffer.allocate(100);
        sourceChannel.read(buf);
        buf.flip();
        byte[] data = new byte[buf.remaining()];
        buf.get(data);
        log.info("读取到数据:{}",new String(data));

    }

    public static void main(String args[]){
        PipeDemo pipeDemo = new PipeDemo();
        pipeDemo.init();

        try{
            pipeDemo.write();
            pipeDemo.read();
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }



    }

}
