package com.communication.io.aio.server;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class AioServer {

    private final int DEFAULT_PORT = 8000;
    private int port = DEFAULT_PORT;

    private AsynchronousServerSocketChannel socketChannel;
    private ExecutorService executorService;

    public AioServer() {
    }

    public AioServer(int port) {
        this.port = port;
    }


    public void init(){

        try{
            socketChannel = AsynchronousServerSocketChannel.open();
            log.info("监听端口{}....",port);
            socketChannel.bind(new InetSocketAddress(port));
            executorService = Executors.newCachedThreadPool();

            while(true){
                Future<AsynchronousSocketChannel> future =  socketChannel.accept();
                AsynchronousSocketChannel channel = future.get();
                log.info("连接成功:{}",channel.getRemoteAddress());
                executorService.submit(new SocketHandler(channel));

            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private class SocketHandler extends Thread{

        private AsynchronousSocketChannel channel;

        private ScheduledExecutorService executorService;

        public SocketHandler(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {

            try{
                log.info("等待接收数据:{}",channel.getRemoteAddress());
            }
            catch(Exception ex){
                log.error(ex.getMessage());
            }

            while (true) {
                try{
                    read(channel);
                    write(channel);
                }
                catch(Exception ex){
                    log.error(ex.getMessage());
                    return;
                }
            }


        }
    }

    private void write(AsynchronousSocketChannel channel)throws Exception{
        byte[] sendData = "服务端返回数据".toString().getBytes("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.allocate(sendData.length);
        byteBuffer.put(sendData);
        byteBuffer.flip();
        channel.write(byteBuffer).get();
    }

    private void read(AsynchronousSocketChannel channel) throws Exception{
        ByteBuffer buffer = ByteBuffer.allocate(1000);


            log.info("read start");
            //阻塞
            int len = channel.read(buffer).get();
            log.info("read end");
            if(len == -1){
                return;
            }
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            log.info("从客户端收到的数据:{}",new String(data,"UTF-8"));

        }



}
