package com.communication.io.nio.server;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;

/**
 *功能描述  nio服务端
 * @author lgj
 * @Description 
 * @date 6/16/20
*/

@Slf4j
public class NioServer {

    private final int DEFAULT_PORT = 8000;
    private int port = DEFAULT_PORT;

    private ServerSocketChannel serverSocketChannel;

    public NioServer(int port) {
        this.port = port;
    }

    public void startServer() throws Exception{

        //打开服务器通道
        serverSocketChannel = ServerSocketChannel.open();


        //配置为非阻塞，必须
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.socket().getChannel();

        log.info("监听端口[{}].....",port);
        //监听端口
        serverSocketChannel.bind(new InetSocketAddress(port));

        ServerSocketChannel ssc = ServerSocketChannel.open( );
        ServerSocket serverSocket = ssc.socket( );

        serverSocket.bind (new InetSocketAddress (1234));



        //开启多路复用器
        Selector selector = Selector.open();
        //绑定多路复用器到channel，并设定关注事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //创建线程进行轮询多路复用器
        new WorkHandle(selector).start();
    }

    /**
     *功能描述 用于轮询select
     * @author lgj
     * @Description 
     * @date 6/16/20
    */
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
                   //监听事件，没有事件发生将阻塞
                   selector.select();
                   //获取事件列表
                   Iterator<SelectionKey> selectionKeyIterator =   selector.selectedKeys().iterator();

                   while (selectionKeyIterator.hasNext()){
                       SelectionKey selectionKey = selectionKeyIterator.next();
                       selectionKeyIterator.remove();
                        //无效事件
                       if(!selectionKey.isValid())
                       {
                           continue;
                       }
                        //连接事件
                       if(selectionKey.isAcceptable()){

                           accept(selectionKey);
                       }
                        //channel有数据可读
                       if(selectionKey.isReadable()){
                           //数据读完有两种操作
                           //1.在当前线程处理，适用于处理时间不长的任务，时间太长会影响其他key的处理
                           //2.创建新线程进行处理，适用于处理时间较长的任务
                           read(selectionKey);
                           write(selectionKey);
                       }
                        //可写事件，最好不要监听可写事件，也就是 不要配置SelectionKey.OP_WRITE
                       //因为在空闲的时候，可写事件一定会发生，导致空轮询
                       if(selectionKey.isWritable()){
                            //write(selectionKey);
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
