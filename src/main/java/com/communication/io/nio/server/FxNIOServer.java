package com.communication.io.nio.server;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;


@Slf4j
public class FxNIOServer {

    private final int DEFAULT_PORT = 8000;
    private int port = DEFAULT_PORT;

    private ServerSocketChannel serverSocketChannel;

    public FxNIOServer(int port) {
        this.port = port;
    }

    public void startServer() throws Exception{

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        log.info("监听端口[{}].....",port);
        serverSocketChannel.bind(new InetSocketAddress(port));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        new WorkHandle(selector).start();


    }

    private class  WorkHandle extends Thread{

        private Selector selector ;


        public WorkHandle(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {

            log.info("事件处理....");
            while (true){

               try{

                   selector.select();

                   Iterator<SelectionKey> selectionKeyIterator =   selector.selectedKeys().iterator();

                   while (selectionKeyIterator.hasNext()){
                       SelectionKey selectionKey = selectionKeyIterator.next();
                       selectionKeyIterator.remove();

                       if(!selectionKey.isValid())
                       {
                           continue;
                       }

                       if(selectionKey.isAcceptable()){

                           accept(selectionKey);
                       }

                       if(selectionKey.isReadable()){
                           read(selectionKey);
                           write(selectionKey);
                       }

                       if(selectionKey.isWritable()){
                            write(selectionKey);
                       }


                   }

               }
               catch(Exception ex){
                   log.error(ex.getMessage());
               }

            }
        }

        public void accept(SelectionKey selectionKey){
            try{
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel channel = serverSocketChannel.accept();

                channel.configureBlocking(false);
                channel.register(selector,SelectionKey.OP_READ);
                log.info("与客户端[{}]连接成功",channel.getRemoteAddress());

            }
            catch(Exception ex){
                log.error(ex.getMessage());
            }

        }

        public void read(SelectionKey selectionKey){

            try{
                SocketChannel channel = (SocketChannel)selectionKey.channel();
                log.info("读取客户端[{}]数据",channel.getRemoteAddress());

                ByteBuffer readByteBUffer = ByteBuffer.allocate(1024);

                int count = channel.read(readByteBUffer);
                if(count == -1){
                    selectionKey.channel().close();
                    selectionKey.cancel();
                    return;
                }
                readByteBUffer.flip();
                byte[] readData = new byte[readByteBUffer.remaining()];
                readByteBUffer.get(readData);
                log.info("接收到来自客户端的数据:"+new String(readData,"UTF-8"));


            }
            catch(Exception ex){
                log.error(ex.getMessage());
            }

        }

        public void write(SelectionKey selectionKey){
            try{
                SocketChannel channel = (SocketChannel)selectionKey.channel();
                log.info("向客户端[{}]写入数据",channel.getRemoteAddress());
                String str = "服务端返回的数据:" + new Random().nextInt(100);
                ByteBuffer writeBuffer = ByteBuffer.wrap(str.getBytes("UTF-8"));
               // writeBuffer.flip();
                channel.write(writeBuffer);
            }
            catch(Exception ex){
                log.error(ex.getMessage());
            }
        }
    }






}