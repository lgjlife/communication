package com.communication.io.nio.client;

import com.communication.io.bio.protocol.Protocol;
import com.communication.io.common.ClientDataHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 *功能描述 
 * @author lgj
 * @Description socket客户端实现类
 * @date 6/15/20
*/
@Slf4j
public class NioClient {


    private final String DEFAULT_HOST =  "127.0.0.1";
    private final int DEFAULT_PORT = 8000;

    private SocketChannel socketChannel;
    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;
    private String name = "socket_client";
    private ScheduledExecutorService reConnectTaskScheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    private final  int reConnectPeriodS = 5;
    private ConnectStatus connectStatus = ConnectStatus.DISCONNECT;


    private ClientDataHandler clientDataHandler;



    public NioClient(String host, int port, String name) {
        this.host = host;
        this.port = port;
        this.name = name;
    }


    /* *
     *功能描述 连接到服务器
     * @author lgj
     * @Description
     * @date 6/15/20
     * @param:
     * @return:
     *
     */
    public void connect(){
        try{
            socketChannel = SocketChannel.open();
            log.info(String.format("Connect to %s:%d ....",host,port));
            //此方法不会阻塞
            socketChannel.connect(new InetSocketAddress(host,port));
            connectStatus = ConnectStatus.CONNECTED;
            log.info(String.format("Connect to %s:%d success ",host,port));
        }
        catch(Exception ex){
            ex.printStackTrace();
            reconnectHandler();
        }
    }

    /* *
     *功能描述 重连操作
     * @author lgj
     * @Description  
     * @date 6/15/20
     * @param: 
     * @return:  void
     *
    */
    private void reconnectHandler(){

        if(connectStatus == ConnectStatus.CONNECTING){
            return;
        }
        connectStatus = ConnectStatus.CONNECTING;
        log.info(String.format("Connect to %s:%d fail",host,port));
        log.info(String.format("ReConnect to %s:%d...",host,port));

        //每5秒进行重连
        ScheduledFuture<?> future =  reConnectTaskScheduledExecutorService.scheduleAtFixedRate(new Thread(){
            @Override
            public void run() {
                connect();
            }
        },reConnectPeriodS,reConnectPeriodS, TimeUnit.SECONDS);
    }
    /* *
     *功能描述 发送请求
     * @author lgj
     * @Description
     * @date 6/15/20
     * @param: data: 需要发送的数据,clientDataHandler: 接收处理
     * @return:  void
     *
    */
    public void request(Object data,ClientDataHandler clientDataHandler) throws Exception {

        write(data);
        read(clientDataHandler);

    }

    public void read(ClientDataHandler clientDataHandler)throws Exception{
        ByteBuffer readBuf = ByteBuffer.allocate(1024);
        socketChannel.read(readBuf);
        readBuf.flip();
        byte[] readData = new byte[readBuf.remaining()];
        readBuf.get(readData);
        clientDataHandler.handler(new String(readData,"UTF-8"));



    }

    public void write(Object data) throws Exception{

        ByteBuffer writeBuf = ByteBuffer.wrap(data.toString().getBytes("UTF-8"));
        socketChannel.write(writeBuf);
    }

    /**
     *功能描述 连接状态
     * @author lgj
     * @Description 
     * @date 6/15/20
    */
    private enum ConnectStatus{
        //断开连接
        DISCONNECT,
        //正在连接
        CONNECTING,
        //连接完成
        CONNECTED;
    }
}
