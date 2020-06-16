package com.communication.io.bio.server;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.SocketHandler;

/**
 *功能描述 bio服务端
 * @author lgj
 * @Description 
 * @date 6/16/20
*/
@Slf4j
public class BioSocketServer {

    private final int DEFAULT_PORT = 8000;
    private int port = DEFAULT_PORT;

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private int threadNum = 14;



    public BioSocketServer() {

    }

    public BioSocketServer(int port) {
        this.port = port;
    }

    public BioSocketServer(int port, int threadNum) {
        this.port = port;
        this.threadNum = threadNum;
    }

    public void acceptTask(){

        try{
            serverSocket = new ServerSocket(port);
            executorService = Executors.newCachedThreadPool();

            while (true){

                log.info("监听端口:{},等待连接.....",port);
                //没有连接，阻塞等待
                Socket socket =  serverSocket.accept();
                log.info("新连接:"+socket.getRemoteSocketAddress());
                //提交线程池管理
                executorService.execute(new SocketHandler(socket));
            }
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }

    }

    private class SocketHandler extends Thread{

        private ConnectStatus connectStatus=ConnectStatus.DISCONNECT;
        private Socket socket ;

        public SocketHandler(Socket socket) {
            this.socket = socket;
            connectStatus = ConnectStatus.CONNECTED;
        }

        @Override
        public void run() {
            System.out.println(String.format("处理:%s的通信",socket.getRemoteSocketAddress()));

            while (true){
                try{
                    read(socket);
                    write(socket);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                finally{
                    if(socket != null){
                        try{
                            socket.close();
                            socket = null;
                        }
                        catch(Exception ex){
                            log.error(ex.getMessage());
                        }

                    }
                }
            }
        }
    }

    private void read( Socket socket ) throws IOException {

        InputStream inputStream = null;

        inputStream = socket.getInputStream();

        byte[] data = new byte[1024];
        //读数据时会阻塞
        int n = inputStream.read(data);

        String str = new String(data,0,n,"UTF-8");
        System.out.println("从客户端收到的数据:"+str);
    }

    private void write( Socket socket ) throws IOException{

        OutputStream outputStream =null;

        outputStream = socket.getOutputStream();
        outputStream.write(("服务端返回:"+new Random().nextInt(100)).getBytes("UTF-8"));
        outputStream.flush();
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
