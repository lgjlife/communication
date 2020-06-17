package com.communication.io.udp.server;


import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;

@Slf4j
public class UdpServer {

    private final int DEFAULT_PORT = 8000;
    private int port = DEFAULT_PORT;

    private DatagramChannel datagramChannel;



    public UdpServer() {
    }

    public UdpServer(int port) {
        this.port = port;
    }


    public void startServer() throws Exception{

        datagramChannel = DatagramChannel.open();
        log.info("绑定:"+port);
        datagramChannel = datagramChannel.bind(new InetSocketAddress(port));

        new Thread(){

            @Override
            public void run() {

                ByteBuffer buf = ByteBuffer.allocate(1024);

                while (true){

                    try{
                        SocketAddress socketAddress =   read(buf,datagramChannel);
                        write(socketAddress,buf,datagramChannel);


                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                        return;
                    }

                }

            }




        }.start();

    }


    public SocketAddress read( ByteBuffer buf,DatagramChannel datagramChannel) throws Exception{
        buf.clear();

        SocketAddress socketAddress = null;
        while (( socketAddress = datagramChannel.receive(buf)) == null){
            Thread.sleep(100);
        }
        log.info("接收数组来自客户端:{}",socketAddress);


        buf.flip();
        byte[] data = new byte[buf.remaining()];
        buf.get(data);
        log.info("接收到的数据:{}",new String(data,"UTF-8"));

        return socketAddress;
    }

    public void write( SocketAddress socketAddress ,ByteBuffer buf,DatagramChannel datagramChannel) throws Exception{


        buf.clear();
        buf.put(("这是服务端返回的数据" + new Date().toString()).getBytes("UTF-8"));
        buf.flip();
        datagramChannel.send(buf,socketAddress);


    }

}
