package com.communication.io.bio.server;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.SocketHandler;

@Slf4j
public class FxSocketServer {

    private final int DEFAULT_PORT = 8000;
    private int port = DEFAULT_PORT;

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private int threadNum = 14;



    public FxSocketServer() {

    }

    public FxSocketServer(int port) {
        this.port = port;
    }

    public FxSocketServer(int port, int threadNum) {
        this.port = port;
        this.threadNum = threadNum;
    }

    public void acceptTask(){

        try{
            serverSocket = new ServerSocket(port);

            executorService = Executors.newCachedThreadPool();
            while (true){
                //没有连接，阻塞等待
                System.out.println(String.format("监听端口:%d,等待连接.....",port));
                Socket socket =  serverSocket.accept();
                System.out.println("新连接:"+socket.getRemoteSocketAddress());
                executorService.execute(new SocketHandler(socket));
            }
        }
        catch(Exception ex){
            log.error(ex.getMessage());
        }

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

    private class SocketHandler extends Thread{

        private ConnectStatus connectStatus=ConnectStatus.DISCONNECT;
        private Socket socket ;
        private BufferedReader reader = null;
        private PrintWriter writer = null;

        private ScheduledExecutorService executorService;

        public SocketHandler(Socket socket) {
            this.socket = socket;
            connectStatus = ConnectStatus.CONNECTED;

            executorService = Executors.newScheduledThreadPool(1);
        }

        @Override
        public void run() {
            System.out.println(String.format("处理:%s的通信",socket.getRemoteSocketAddress()));

            while (true){
                InputStream inputStream = null;
                OutputStream outputStream =null;
                try{
                    inputStream = socket.getInputStream();

                    byte[] data = new byte[1024];
                    //读数据时会阻塞
                    int n = inputStream.read(data);

                    String str = new String(data,0,n,"UTF-8");
                    System.out.println("从客户端收到的数据:"+str);

                    outputStream = socket.getOutputStream();
                    outputStream.write(("服务端返回:"+new Random().nextInt(100)).getBytes("UTF-8"));
                    outputStream.flush();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                finally{

                }
            }
        }
    }
}
