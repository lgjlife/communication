package com.communication.io.udp.client;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import java.util.Date;

@Slf4j
public class UdpClient {

    private final String DEFAULT_HOST =  "127.0.0.1";
    private final int DEFAULT_PORT = 8000;

    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;

    private DatagramChannel datagramChannel;


    public UdpClient() {
        connect();
    }

    public UdpClient(String host, int port) {
        this.host = host;
        this.port = port;
        connect();

    }

    public void connect(){

        try{
            log.info("正在连接:[{}:{}]....",host,port);
            datagramChannel = DatagramChannel.open();
            datagramChannel.connect(new InetSocketAddress(port));
            log.info("连接:[{}:{}]成功",host,port);
        }
        catch(Exception ex){
            log.error(ex.getMessage());
            log.info("连接:[{}:{}]失败",host,port);
        }


    }





    public void read( ) throws Exception{
        ByteBuffer buf = ByteBuffer.allocate(1000);
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

    }

    public void write( ) throws Exception{
        log.info("发送数据....");
        ByteBuffer buf = ByteBuffer.allocate(100);
        buf.put(("这是客户端的数据" + new Date().toString()).getBytes("utf-8"));
        buf.flip();
        datagramChannel.write(buf);


    }
}
