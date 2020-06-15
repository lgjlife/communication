package com.communication.io.aio.client;

import com.communication.io.bio.client.FxSocketClient;
import com.communication.io.bio.common.ClientDataHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Slf4j
public class FxAioClient {

    private final String DEFAULT_HOST =  "127.0.0.1";
    private final int DEFAULT_PORT = 8000;

    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;
    private String name = "socket_client";
    private final  int reConnectPeriodS = 5;
    private AsynchronousSocketChannel asynSocketChannel ;

    private ScheduledExecutorService reConnectTaskScheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    private ConnectStatus connectStatus = ConnectStatus.DISCONNECT;

    public FxAioClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void connect(){


        try{
            asynSocketChannel = AsynchronousSocketChannel.open();

            SocketAddress address = new InetSocketAddress(host,port);
            asynSocketChannel.connect(address, null, new CompletionHandler<Void, Object>() {
                @Override
                public void completed(Void result, Object attachment) {

                    try{
                        log.info("连接{}成功！",asynSocketChannel.getRemoteAddress());
                        connectStatus = ConnectStatus.CONNECTED;
                    }
                    catch(Exception ex){

                    }


                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    try{

                        log.info("连接{}失败！",asynSocketChannel.getRemoteAddress());
                        log.info("重新连接{}....",asynSocketChannel.getRemoteAddress());
                        reconnectHandler();
                    }
                    catch(Exception ex){
                        log.error(ex.getMessage());
                    }
                }
            });

        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }


    }

    public void request(Object data, ClientDataHandler handle) throws Exception{

        write(asynSocketChannel,data);
        read(asynSocketChannel,handle);
    }

    private void write(AsynchronousSocketChannel asynSocketChannel ,Object data) throws Exception{

        byte[] sendData = data.toString().getBytes("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.allocate(sendData.length);
        byteBuffer.put(sendData);
        byteBuffer.flip();
        asynSocketChannel.write(byteBuffer).get();
    }

    private void read(AsynchronousSocketChannel asynSocketChannel , ClientDataHandler handle){
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(1024);
        log.info("读取数据");
        asynSocketChannel.read(byteBuffer1, byteBuffer1, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {

                log.info("attachment = " + attachment);
                attachment.flip();
                byte[] recData = new byte[attachment.remaining()];
                attachment.get(recData);
                String str = null;
                try{
                    str = new String(recData,0,result,"UTF-8");
                }
                catch(Exception ex){
                    log.error(ex.getMessage());
                }

                handle.handler(str);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

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
