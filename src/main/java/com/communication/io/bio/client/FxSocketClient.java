package com.communication.io.bio.client;

import com.communication.io.bio.protocol.Protocol;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
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


    private ClientDataHandler clientDataHandler;



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
    /* *
     *功能描述 发送请求
     * @author lgj
     * @Description
     * @date 6/15/20
     * @param: data: 需要发送的数据,clientDataHandler: 接收处理
     * @return:  void
     *
    */
    public void request(Object data,ClientDataHandler clientDataHandler) throws IOException {


        InputStream inputStream = null;
        OutputStream outputStream =null;

        byte[] reqData= createData(data);
        outputStream = socket.getOutputStream();
        outputStream.write(reqData);
        outputStream.flush();


        inputStream = socket.getInputStream();
        log.info("available = "+inputStream.available());

        byte[] recData = new byte[1000];
        int n = inputStream.read(recData);

        String str = new String(recData,0,n,"UTF-8");
        clientDataHandler.handler(str);

    }

    private  byte[] createData(Object object){

        byte[] reqData= {};

        Protocol protocol = new Protocol();
        protocol.setId(1);
        protocol.setTimestamp(new Date().getTime());


        try{
            reqData =   object.toString().getBytes("UTF-8");
        }
        catch(Exception ex){
            reqData = new byte[]{};
        }

        return reqData;

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
