package com.communication.netty.client;

import com.communication.netty.client.handler.*;
import com.communication.netty.exception.UnConnectServerException;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import com.sun.javafx.scene.control.skin.LabeledImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.BootstrapConfig;
import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;


import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class NettyClient {

    private ConcurrentHashMap<ServerDef,Channel> serverCache = new ConcurrentHashMap<>();

    //工作线程池,默认cpu * 2
    EventLoopGroup worker = new NioEventLoopGroup();

    ExecutorService reconectExecutorService  = Executors.newFixedThreadPool(10);

    private Bootstrap bootstrap;

    private ConcurrentHashMap<ServerDef,ConnectState> connectStates = new ConcurrentHashMap();

    public NettyClient() {
        buildBootstrap();
    }

    public void buildBootstrap(){

        bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,100);
        bootstrap.handler(new ChannelInitializer(){
            @Override
            protected void initChannel(Channel channel) throws Exception {

                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new ClientChannelTrafficShapingHandler());
                //   pipeline.addLast(new ChannelTrafficShapingHandler(1024,1024));

//                 //   pipeline.addLast( new ClientIdleStateHandller(0,4,1, TimeUnit.SECONDS));
//                    pipeline.addLast("2", new InboundHandlerB());
//                    pipeline.addLast("3", new OutboundHandlerA());
//                    pipeline.addLast("4", new OutboundHandlerB());
//                   // pipeline.addLast("5", new InboundOutboundHandlerX());
//
//                    //输入
//                    pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,0,1,0,1));
//                    pipeline.addLast(new StringDecoder());
//
//                    pipeline.addLast("5",new ClientChannelInboundHandlerAdapter());
//                    //输出
//                    channel.pipeline().addLast("6",new ClientChannelOutboundHandlerAdapter());
            }
        });


    }

    public void connect(String host,int port){

        ServerDef serverDef = new ServerDef(host,port);

        try{


            log.info("正在连接[{}:{}]...",host,port);



            ChannelFuture channelFuture =  bootstrap.connect(host,port).sync();

            Channel channel =  channelFuture.channel();
            serverCache.put(serverDef,channel);
            log.info("连接[{}:{}]成功！",host,port);
            connectStates.put(serverDef,ConnectState.Connected);

            ChannelConfig channelConfig = channel.config();

            Map<ChannelOption<?>, Object> ops = channelConfig.getOptions();
            ops.forEach((key,val)->{
                //log.info("{}　　{} ",key,val);
            });
        }
        catch(Exception ex){
            log.error("连接{}:{}失败",host,port);
            log.error(ex.getMessage());
            reConect(serverDef,host, port);
        }

    }

    private void reConect(ServerDef serverDef , String host, int port){
        log.info("正在重新连接[{}:{}]...",host,port);

        reconectExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try{

                    Thread.sleep(5000);
                    connect(host,port);
                }
                catch(Exception ex){
                    log.error(ex.getMessage());
                }
            }
        });





    }


    public Object send(String host ,int port ,Object data) throws UnConnectServerException{

        Channel channel = serverCache.get(new ServerDef(host, port));

        if(channel == null){
            throw  new UnConnectServerException(String.format("Unconnect to %s:%d",host,port));
        }

        log.info("发送数据....");
        try{
            ByteBuf message = Unpooled.buffer(((byte[])data).length+1);
            message.writeByte(5);
            message.writeBytes((byte[])data);

            channel.writeAndFlush(message).sync();
            log.info("数据发送完成....");

        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        return  channel.read();
    }

    public void send(String host ,int port, byte[] data) throws Exception{


        Channel channel = serverCache.get(new ServerDef(host, port));
        if(channel == null){
            throw  new UnConnectServerException(String.format("Unconnect to %s:%d",host,port));
        }
        if (channel.isOpen()){

            ByteBuf message = Unpooled.buffer(data.length);
            message.writeBytes(data);
            channel.writeAndFlush(message);
        }
        else {
            log.info("与服务端未连接");
        }


    }

    class ServerDef{

        private String host;
        private int port;

        public ServerDef(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 37 * result + host.hashCode();
            result = 37 * result + port;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null ){
                return false;
            }
            if(!(obj instanceof ServerDef)){
                return false;
            }
            if( (this.host.equals(((ServerDef) obj).host)
                && (this.port == ((ServerDef) obj).port)))
            {
                return true;
            }
            return false;

        }
    }

    private enum ConnectState{
        Connecting,
        Connected,
        Unconnect,
    }

    public static void main(String args[]){
        for(int i = 0; i< 10; i++){
            ServerDef serverDef = new NettyClient().new ServerDef("127.0.0.1"+i,8001+i);
            ServerDef serverDef1 = new NettyClient().new ServerDef("127.0.0.1"+i,8001+i);
            System.out.println(serverDef.equals(serverDef1));
        }
    }
}
