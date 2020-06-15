package com.communication.io.bio.client;

import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.util.concurrent.*;


/**
 *功能描述 
 * @author lgj
 * @Description socket客户端实现类
 * @date 6/15/20
*/
@Slf4j
public class FxSocketClient {


    private final String DEFAULT_HOST =  "127.0.0.1";
    private final int DEFAULT_PORT = 8000;

    private Socket socket;
    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;
    private String name = "socket_client";
    private ScheduledExecutorService reConnectTaskScheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    private final  int reConnectPeriodS = 5;
    private ConnectStatus connectStatus = ConnectStatus.DISCONNECT;



    public FxSocketClient(String host, int port, String name) {
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
            log.info(String.format("Connect to %s:%d ....",host,port));
            //此方法不会阻塞
            socket = new Socket(host,port);
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
