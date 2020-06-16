package com.communication.io.file;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public class AioFileChannel {

    private String fileName;
    private OpenOption[]  options;
    private AsynchronousFileChannel channel;

    public AioFileChannel(String fileName,OpenOption... options) {
        this.fileName = fileName;
        this.options = options;
        try{
            channel = AsynchronousFileChannel.open( Paths.get(fileName),options);
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }
    }

    public void read(int pos){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        channel.read(byteBuffer, pos, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                attachment.flip();
                if(attachment.remaining() > 0){

                    byte[] data = new byte[attachment.remaining()];
                    attachment.get(data);

                    try{
                        log.info("AIO读取到的数据:"+ new String(data,"UTF-8"));
                    }
                    catch(Exception ex){
                        log.error(ex.getMessage());
                    }

                }
                else {
                    log.info("没有读取到数据");
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                log.info("AIO数据读取失败");
            }
        });
    }

    public void write(String data,int pos) throws Exception{


        ByteBuffer buf = ByteBuffer.allocate(100);
        buf.clear();
        buf.put(data.getBytes("UTF-8"));
        buf.flip();
        channel.write(buf, pos, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                log.info("AIO数据写入成功");
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                log.info("AIO数据写入失败");
            }
        });
        channel.force(true);




    }
}
